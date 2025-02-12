package com.ataglance.walletglance.record.domain.usecase

import kotlinx.coroutines.flow.Flow

interface GetLastRecordNumUseCase {

    fun getAsFlow(): Flow<Int?>

    suspend fun get(): Int?

}