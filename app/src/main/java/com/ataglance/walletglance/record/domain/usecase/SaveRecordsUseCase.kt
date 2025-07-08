package com.ataglance.walletglance.record.domain.usecase

import com.ataglance.walletglance.transaction.domain.model.RecordWithItems

interface SaveRecordsUseCase {

    suspend fun execute(recordWithItems: List<RecordWithItems>)

}