package com.ataglance.walletglance.settings.domain.usecase

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

class ApplyLanguageToSystemUseCaseImpl : ApplyLanguageToSystemUseCase {
    override fun execute(langCode: String) {
        val appLocale = LocaleListCompat.forLanguageTags(langCode)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }
}