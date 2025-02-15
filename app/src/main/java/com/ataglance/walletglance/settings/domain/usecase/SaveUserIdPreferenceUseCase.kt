package com.ataglance.walletglance.settings.domain.usecase

interface SaveUserIdPreferenceUseCase {
    suspend fun save(userId: String)
}