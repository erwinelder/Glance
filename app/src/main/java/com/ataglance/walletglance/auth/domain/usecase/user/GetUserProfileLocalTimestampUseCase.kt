package com.ataglance.walletglance.auth.domain.usecase.user

import kotlinx.coroutines.flow.Flow

interface GetUserProfileLocalTimestampUseCase {

    suspend fun get(): Long

    fun getAsFlow(): Flow<Long>

}