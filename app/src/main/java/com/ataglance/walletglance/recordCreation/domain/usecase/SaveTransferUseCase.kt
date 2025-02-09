package com.ataglance.walletglance.recordCreation.domain.usecase

import com.ataglance.walletglance.recordCreation.domain.transfer.CreatedTransfer

interface SaveTransferUseCase {
    suspend fun execute(transfer: CreatedTransfer)
}