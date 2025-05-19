package com.ataglance.walletglance.settings.domain.usecase.language

interface SaveLanguageToPreferencesUseCase {
    suspend fun execute(langCode: String, timestamp: Long)
}