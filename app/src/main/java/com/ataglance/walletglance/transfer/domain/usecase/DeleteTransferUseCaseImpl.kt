package com.ataglance.walletglance.transfer.domain.usecase

import com.ataglance.walletglance.account.domain.usecase.GetAccountsUseCase
import com.ataglance.walletglance.account.domain.usecase.SaveAccountsUseCase
import com.ataglance.walletglance.account.mapper.toDataModel
import com.ataglance.walletglance.transfer.data.repository.TransferRepository

class DeleteTransferUseCaseImpl(
    private val transferRepository: TransferRepository,
    private val getAccountsUseCase: GetAccountsUseCase,
    private val saveAccountsUseCase: SaveAccountsUseCase
) : DeleteTransferUseCase {

    override suspend fun execute(transferId: Long) {
        val transfer = transferRepository.getTransfer(id = transferId) ?: return

        val senderAccount = getAccountsUseCase.get(id = transfer.senderAccountId)
            ?.addToBalance(amount = transfer.senderAmount)
            ?: return
        val receiverAccount = getAccountsUseCase.get(id = transfer.receiverAccountId)
            ?.subtractFromBalance(amount = transfer.receiverAmount)
            ?: return
        val accounts = listOf(senderAccount, receiverAccount)

        saveAccountsUseCase.save(accounts = accounts.map { it.toDataModel() })
        transferRepository.deleteTransfer(transfer = transfer)
    }

}