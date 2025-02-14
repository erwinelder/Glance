package com.ataglance.walletglance.settings.domain.usecase

import com.ataglance.walletglance.core.data.repository.SettingsRepository

class SaveLanguagePreferenceUseCaseImpl(
    private val settingsRepository: SettingsRepository
) : SaveLanguagePreferenceUseCase {
    override suspend fun save(langCode: String) {
        settingsRepository.saveLanguagePreference(langCode)
    }
}