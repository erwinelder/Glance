package com.ataglance.walletglance.settings.domain.usecase

interface SaveLanguagePreferenceUseCase {
    suspend fun save(langCode: String)
}