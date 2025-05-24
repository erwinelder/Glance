package com.ataglance.walletglance.notification.domain.usecase

import com.ataglance.walletglance.settings.data.repository.SettingsRepository

class SaveTurnOnDailyRecordsReminderPreferenceUseCaseImpl(
    private val settingsRepository: SettingsRepository
): SaveTurnOnDailyRecordsReminderPreferenceUseCase {
    override suspend fun execute(turnOn: Boolean) {
        settingsRepository.saveTurnOnDailyRecordsReminderPreference(turnOn)
    }
}