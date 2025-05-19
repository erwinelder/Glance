package com.ataglance.walletglance.settings.domain.usecase

import kotlinx.coroutines.flow.Flow

interface GetUserProfileLocalTimestampUseCase {

    suspend fun get(): Long

    fun getAsFlow(): Flow<Long>

}