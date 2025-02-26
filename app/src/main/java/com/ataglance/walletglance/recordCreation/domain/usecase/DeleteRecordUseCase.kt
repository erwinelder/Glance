package com.ataglance.walletglance.recordCreation.domain.usecase

interface DeleteRecordUseCase {
    suspend fun execute(recordNum: Int)
}