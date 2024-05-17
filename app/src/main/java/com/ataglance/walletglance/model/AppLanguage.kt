package com.ataglance.walletglance.model

sealed class AppLanguage(val languageCode: String, val languageNativeName: String) {
    data object Czech : AppLanguage("cs", "Čeština")
    data object English : AppLanguage("en", "English")
    data object German : AppLanguage("de", "Deutsch")
    data object Russian : AppLanguage("ru", "Русский")
    data object Spanish : AppLanguage("es", "Español")
    data object Ukrainian : AppLanguage("uk", "Українська")
}