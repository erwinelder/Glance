package com.ataglance.walletglance.notification.domain.usecase

import com.ataglance.walletglance.settings.data.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class GetTurnOnDailyRecordsReminderPreferenceUseCaseImpl(
    private val settingsRepository: SettingsRepository
) : GetTurnOnDailyRecordsReminderPreferenceUseCase {

    override fun getAsFlow(): Flow<Boolean> {
        return settingsRepository.turnOnDailyRecordsReminder
    }

    override suspend fun get(): Boolean {
        return settingsRepository.turnOnDailyRecordsReminder.firstOrNull() ?: false
    }

}