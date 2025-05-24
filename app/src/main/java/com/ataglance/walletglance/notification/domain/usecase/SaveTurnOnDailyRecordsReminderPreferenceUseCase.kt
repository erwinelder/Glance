package com.ataglance.walletglance.notification.domain.usecase

interface SaveTurnOnDailyRecordsReminderPreferenceUseCase {
    suspend fun execute(turnOn: Boolean)
}