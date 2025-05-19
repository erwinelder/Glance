package com.ataglance.walletglance.settings.domain.usecase.language

interface SaveLanguageUseCase {
    suspend fun execute(langCode: String)
}