package com.ataglance.walletglance.settings.domain.usecase

import com.ataglance.walletglance.settings.data.repository.SettingsRepository

class ChangeAppSetupStatusUseCaseImpl(
    private val settingsRepository: SettingsRepository
) : ChangeAppSetupStatusUseCase {

    override suspend fun preFinishSetup() {
        settingsRepository.saveIsSetUpPreference(2)
    }

    override suspend fun finishSetup() {
        settingsRepository.saveIsSetUpPreference(1)
    }
}