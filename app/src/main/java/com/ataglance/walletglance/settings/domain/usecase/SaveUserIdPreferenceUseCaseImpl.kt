package com.ataglance.walletglance.settings.domain.usecase

import com.ataglance.walletglance.settings.data.repository.SettingsRepository

class SaveUserIdPreferenceUseCaseImpl(
    private val settingsRepository: SettingsRepository
) : SaveUserIdPreferenceUseCase {
    override suspend fun save(userId: String) {
        settingsRepository.saveUserIdPreference(userId)
    }
}