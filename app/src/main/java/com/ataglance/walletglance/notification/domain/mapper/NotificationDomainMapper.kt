package com.ataglance.walletglance.notification.domain.mapper

import androidx.core.app.NotificationCompat
import com.ataglance.walletglance.notification.domain.model.AppNotificationPriority


fun AppNotificationPriority.toNativePriority(): Int {
    return when (this) {
        AppNotificationPriority.Default -> NotificationCompat.PRIORITY_DEFAULT
    }
}