package com.ataglance.walletglance.notification.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.notification.domain.model.AppNotificationType
import com.ataglance.walletglance.notification.domain.model.NotificationManager
import com.ataglance.walletglance.notification.domain.usecase.GetTurnOnDailyRecordsReminderPreferenceUseCase
import com.ataglance.walletglance.notification.domain.usecase.SaveTurnOnDailyRecordsReminderPreferenceUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotificationsViewModel(
    getTurnOnDailyRecordsReminderPreferenceUseCase: GetTurnOnDailyRecordsReminderPreferenceUseCase,
    private val saveTurnOnDailyRecordsReminderPreferenceUseCase: SaveTurnOnDailyRecordsReminderPreferenceUseCase,
) : ViewModel() {

    val dailyRecordsReminderTurnedOn = getTurnOnDailyRecordsReminderPreferenceUseCase
        .getAsFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    fun setDailyRecordsReminder(turnOn: Boolean) {
        viewModelScope.launch {
            saveTurnOnDailyRecordsReminderPreferenceUseCase.execute(turnOn = turnOn)

            if (turnOn) {
                NotificationManager.scheduleNotification(
                    notificationType = AppNotificationType.DailyRecordsReminder
                )
            } else {
                NotificationManager.cancelScheduledNotification(
                    notificationType = AppNotificationType.DailyRecordsReminder
                )
            }
        }
    }

    fun checkNotificationPermission(): Boolean {
        return NotificationManager.checkNotificationPermission()
    }


    private val _showNotificationsDeniedDialog = MutableStateFlow(false)
    val showNotificationsDeniedDialog = _showNotificationsDeniedDialog.asStateFlow()

    fun setShowNotificationsDeniedDialog(value: Boolean) {
        _showNotificationsDeniedDialog.update { value }
    }

}