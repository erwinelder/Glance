package com.ataglance.walletglance.transfer.domain.usecase

import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.usecase.GetAccountsUseCase
import com.ataglance.walletglance.account.domain.utils.findById
import com.ataglance.walletglance.account.domain.utils.getOtherFrom
import com.ataglance.walletglance.transfer.mapper.toDraft
import com.ataglance.walletglance.transfer.presentation.model.TransferDraft

class GetTransferDraftUseCaseImpl(
    private val getTransferUseCase: GetTransferUseCase,
    private val getAccountsUseCase: GetAccountsUseCase
) : GetTransferDraftUseCase {

    override suspend fun execute(
        transferId: Long?,
        accountId: Int?,
        accounts: List<Account>?
    ): TransferDraft {
        val accounts = accounts ?: getAccountsUseCase.getAll()

        return transferId
            ?.let { getTransferUseCase.get(id = transferId) }
            ?.toDraft(accounts = accounts)
            ?: run {
                val senderAccount = accountId?.let { accounts.findById(accountId) }
                val receiverAccount = senderAccount?.let {
                    accounts.getOtherFrom(account = senderAccount)
                }
                TransferDraft.asNew(
                    senderAccount = senderAccount,
                    receiverAccount = receiverAccount
                )
            }
    }

}