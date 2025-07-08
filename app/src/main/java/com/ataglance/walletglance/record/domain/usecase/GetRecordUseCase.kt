package com.ataglance.walletglance.record.domain.usecase

import com.ataglance.walletglance.transaction.domain.model.RecordWithItems

interface GetRecordUseCase {

    suspend fun get(id: Long): RecordWithItems?

}