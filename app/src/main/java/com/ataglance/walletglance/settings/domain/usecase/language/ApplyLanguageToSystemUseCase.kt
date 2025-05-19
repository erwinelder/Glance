package com.ataglance.walletglance.settings.domain.usecase.language

interface ApplyLanguageToSystemUseCase {
    fun execute(langCode: String)
}