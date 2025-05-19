package com.ataglance.walletglance.settings.domain.usecase.language

class SaveLanguageLocallyUseCaseImpl(
    private val saveLanguageToPreferencesUseCase: SaveLanguageToPreferencesUseCase,
    private val applyLanguageToSystemUseCase: ApplyLanguageToSystemUseCase
) : SaveLanguageLocallyUseCase {
    override suspend fun execute(langCode: String, timestamp: Long) {
        saveLanguageToPreferencesUseCase.execute(langCode = langCode, timestamp = timestamp)
        applyLanguageToSystemUseCase.execute(langCode = langCode)
    }
}