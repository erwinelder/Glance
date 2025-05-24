package com.ataglance.walletglance.auth.domain.usecase.user

interface SaveUserProfileTimestampUseCase {
    suspend fun execute(timestamp: Long)
}