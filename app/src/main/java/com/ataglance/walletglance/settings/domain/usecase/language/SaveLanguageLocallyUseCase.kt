package com.ataglance.walletglance.settings.domain.usecase.language

interface SaveLanguageLocallyUseCase {
    suspend fun execute(langCode: String, timestamp: Long)
}