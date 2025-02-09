package com.ataglance.walletglance.record.domain.usecase

import com.ataglance.walletglance.record.domain.model.RecordStack

interface GetRecordStackUseCase {
    suspend fun get(recordNum: Int): RecordStack?
}