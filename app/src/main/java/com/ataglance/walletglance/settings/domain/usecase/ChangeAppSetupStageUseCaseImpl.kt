package com.ataglance.walletglance.settings.domain.usecase

import com.ataglance.walletglance.settings.data.repository.SettingsRepository
import kotlinx.coroutines.flow.firstOrNull

class ChangeAppSetupStageUseCaseImpl(
    private val settingsRepository: SettingsRepository
) : ChangeAppSetupStageUseCase {

    override suspend fun preFinishSetup() {
        settingsRepository.saveIsSetUpPreference(2)
    }

    override suspend fun finishSetup() {
        settingsRepository.saveIsSetUpPreference(1)
    }

    override suspend fun updateSetupStageInNeeded() {
        val isSetUp = settingsRepository.setupStage.firstOrNull()
        if (isSetUp == 2) {
            finishSetup()
        }
    }

}