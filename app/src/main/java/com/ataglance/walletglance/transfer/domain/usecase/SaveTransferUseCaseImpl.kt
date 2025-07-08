package com.ataglance.walletglance.transfer.domain.usecase

import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.usecase.GetAccountsUseCase
import com.ataglance.walletglance.account.domain.usecase.SaveAccountsUseCase
import com.ataglance.walletglance.account.mapper.toDataModel
import com.ataglance.walletglance.transfer.data.repository.TransferRepository
import com.ataglance.walletglance.transaction.domain.model.Transfer
import com.ataglance.walletglance.transfer.mapper.toDataModel

class SaveTransferUseCaseImpl(
    private val transferRepository: TransferRepository,
    private val getAccountsUseCase: GetAccountsUseCase,
    private val saveAccountsUseCase: SaveAccountsUseCase
) : SaveTransferUseCase {

    override suspend fun execute(transfer: Transfer) {
        val accounts = if (transfer.isNew) {
            getAccountsAfterNewTransfer(transfer = transfer)
        } else {
            getAccountsAfterEditedTransfer(transfer = transfer)
        } ?: return

        saveAccountsUseCase.save(accounts = accounts.map { it.toDataModel() })
        transferRepository.upsertTransfer(transfer = transfer.toDataModel())
    }

    private suspend fun getAccountsAfterNewTransfer(transfer: Transfer): List<Account>? {
        val senderAccount = getAccountsUseCase.get(id = transfer.sender.accountId)
            ?.addToBalance(amount = transfer.sender.amount)
            ?: return null
        val receiverAccount = getAccountsUseCase.get(id = transfer.receiver.accountId)
            ?.subtractFromBalance(amount = transfer.receiver.amount)
            ?: return null
        return listOf(senderAccount, receiverAccount)
    }

    private suspend fun getAccountsAfterEditedTransfer(transfer: Transfer): List<Account>? {
        val currentTransfer = transferRepository.getTransfer(id = transfer.id) ?: return null
        val accounts = mutableMapOf<Int, Account>()

        getAccountsUseCase.get(id = currentTransfer.senderAccountId)?.let { account ->
            accounts[currentTransfer.senderAccountId] = account
                .addToBalance(amount = currentTransfer.senderAmount)
        } ?: return null
        getAccountsUseCase.get(id = currentTransfer.receiverAccountId)?.let { account ->
            accounts[currentTransfer.receiverAccountId] = account
                .subtractFromBalance(amount = currentTransfer.receiverAmount)
        } ?: return null

        val senderAccount = accounts[transfer.senderAccountId]
            ?: getAccountsUseCase.get(id = transfer.senderAccountId)
            ?: return null
        accounts[transfer.senderAccountId] = senderAccount
            .subtractFromBalance(amount = transfer.senderAmount)

        val receiverAccount = accounts[transfer.receiverAccountId]
            ?: getAccountsUseCase.get(id = transfer.receiverAccountId)
            ?: return null
        accounts[transfer.receiverAccountId] = receiverAccount
            .addToBalance(amount = transfer.receiverAmount)

        return accounts.values.toList()
    }

}