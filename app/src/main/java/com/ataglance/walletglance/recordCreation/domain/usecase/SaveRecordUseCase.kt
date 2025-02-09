package com.ataglance.walletglance.recordCreation.domain.usecase

import com.ataglance.walletglance.recordCreation.domain.record.CreatedRecord

interface SaveRecordUseCase {
    suspend fun execute(record: CreatedRecord)
}