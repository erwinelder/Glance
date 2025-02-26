package com.ataglance.walletglance.record.domain.usecase

import kotlinx.coroutines.flow.Flow

interface GetLastRecordNumUseCase {

    fun getFlow(): Flow<Int?>

    suspend fun get(): Int?

    suspend fun getNext(): Int

}