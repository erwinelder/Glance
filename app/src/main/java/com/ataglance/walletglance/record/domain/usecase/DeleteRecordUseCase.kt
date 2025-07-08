package com.ataglance.walletglance.record.domain.usecase

interface DeleteRecordUseCase {

    suspend fun execute(id: Long)

}