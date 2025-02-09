package com.ataglance.walletglance.recordCreation.domain.usecase

import com.ataglance.walletglance.recordCreation.domain.transfer.TransferUnitsRecordNums

interface DeleteTransferUseCase {
    suspend fun execute(unitsRecordNums: TransferUnitsRecordNums)
}