package com.ataglance.walletglance.record.domain.usecase

import com.ataglance.walletglance.recordCreation.domain.transfer.TransferPairRecordStacks
import com.ataglance.walletglance.recordCreation.domain.transfer.TransferUnitsRecordNums

interface GetTransferPairUseCase {
    suspend fun execute(unitsRecordNums: TransferUnitsRecordNums): TransferPairRecordStacks?
}