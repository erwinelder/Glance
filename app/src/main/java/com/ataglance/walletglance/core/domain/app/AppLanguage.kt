package com.ataglance.walletglance.core.domain.app

sealed class AppLanguage(val languageCode: String, val languageNativeName: String) {

    data object Czech : AppLanguage("cs", "Čeština")
    data object English : AppLanguage("en", "English")
    data object German : AppLanguage("de", "Deutsch")
    data object Russian : AppLanguage("ru", "Русский")
    data object Spanish : AppLanguage("es", "Español")
    data object Ukrainian : AppLanguage("uk", "Українська")

    companion object {

        fun fromLangCode(langCode: String): AppLanguage? {
            return when (langCode) {
                Czech.languageCode -> Czech
                English.languageCode -> English
                German.languageCode -> German
                Russian.languageCode -> Russian
                Spanish.languageCode -> Spanish
                Ukrainian.languageCode -> Ukrainian
                else -> null
            }
        }

    }

}