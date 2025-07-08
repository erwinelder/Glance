package com.ataglance.walletglance.record.domain.usecase

import com.ataglance.walletglance.transaction.domain.model.RecordWithItems

interface SaveRecordUseCase {

    suspend fun execute(createdRecord: RecordWithItems)

}