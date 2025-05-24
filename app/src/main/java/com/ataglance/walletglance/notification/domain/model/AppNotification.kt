package com.ataglance.walletglance.notification.domain.model

import androidx.annotation.StringRes
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.presentation.MainActivity
import java.util.concurrent.TimeUnit

sealed class AppNotification(
    val id: Int,
    val tag: String,
    val channel: AppNotificationChannel,
    @StringRes val title: Int,
    @StringRes val message: Int,
    val priority: AppNotificationPriority,
    val targetActivity: Class<*>
) {

    data object DailyRecordsReminder : AppNotification(
        id = 1,
        tag = "daily_records_reminder",
        channel = AppNotificationChannel.General,
        title = R.string.daily_records_reminder_notification_title,
        message = R.string.daily_records_reminder_notification_message,
        priority = AppNotificationPriority.Default,
        targetActivity = MainActivity::class.java
    ) {

        fun getDelayBeforeFirstNotification(): Pair<Long, TimeUnit> {
            return Pair(1, TimeUnit.DAYS)
        }

    }


    fun getType(): AppNotificationType {
        return when (this) {
            is DailyRecordsReminder -> AppNotificationType.DailyRecordsReminder
        }
    }

    fun getUniquePeriodicWorkName(): String {
        return "channel_id_${channel.id}_${tag}"
    }

    fun getRepeatInterval(): Pair<Long, TimeUnit>? {
        return when (this) {
            is DailyRecordsReminder -> Pair(1, TimeUnit.DAYS)
        }
    }

    fun getDelayBeforeRepeatInterval(): Pair<Long, TimeUnit>? {
        return when (this) {
            is DailyRecordsReminder -> getDelayBeforeFirstNotification()
        }
    }


    companion object {

        fun fromType(type: AppNotificationType): AppNotification {
            return when (type) {
                AppNotificationType.DailyRecordsReminder -> DailyRecordsReminder
            }
        }

    }

}