package com.ataglance.walletglance.settings.domain.usecase

interface SaveUserProfileTimestampUseCase {
    suspend fun execute(timestamp: Long)
}