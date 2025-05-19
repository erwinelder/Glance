package com.ataglance.walletglance.settings.domain.usecase.language

import com.ataglance.walletglance.settings.data.repository.SettingsRepository

class SaveLanguageToPreferencesUseCaseImpl(
    private val settingsRepository: SettingsRepository
) : SaveLanguageToPreferencesUseCase {
    override suspend fun execute(langCode: String, timestamp: Long) {
        settingsRepository.saveLanguagePreference(langCode = langCode)
        settingsRepository.saveUserProfileTimestamp(timestamp = timestamp)
    }
}