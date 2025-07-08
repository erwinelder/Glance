package com.ataglance.walletglance.transfer.domain.usecase

import com.ataglance.walletglance.transaction.domain.model.Transfer

interface GetTransfersByAccountsUseCase {

    suspend fun execute(accountIds: List<Int>): List<Transfer>

}