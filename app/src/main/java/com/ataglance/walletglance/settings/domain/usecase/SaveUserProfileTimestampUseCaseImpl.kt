package com.ataglance.walletglance.settings.domain.usecase

import com.ataglance.walletglance.settings.data.repository.SettingsRepository

class SaveUserProfileTimestampUseCaseImpl(
    private val settingsRepository: SettingsRepository
) : SaveUserProfileTimestampUseCase {
    override suspend fun execute(timestamp: Long) {
        settingsRepository.saveUserProfileTimestamp(timestamp = timestamp)
    }
}