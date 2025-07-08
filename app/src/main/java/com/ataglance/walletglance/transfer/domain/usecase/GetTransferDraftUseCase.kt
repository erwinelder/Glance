package com.ataglance.walletglance.transfer.domain.usecase

import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.transfer.presentation.model.TransferDraft

interface GetTransferDraftUseCase {

    suspend fun execute(
        transferId: Long?,
        accountId: Int?,
        accounts: List<Account>? = null
    ): TransferDraft

}