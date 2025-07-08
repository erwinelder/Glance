package com.ataglance.walletglance.transfer.domain.usecase

import com.ataglance.walletglance.transaction.domain.model.Transfer

interface SaveTransferUseCase {

    suspend fun execute(transfer: Transfer)

}