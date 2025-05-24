package com.ataglance.walletglance.notification.domain.model

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.presentation.model.ResourceManager
import com.ataglance.walletglance.notification.domain.mapper.toNativePriority
import org.koin.core.context.GlobalContext

object NotificationManager {

    private fun getNativeNotificationManager(): NotificationManager {
        val context = GlobalContext.get().get<Context>()
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private fun setNotificationContent(notification: AppNotification): NotificationCompat.Builder {
        val context = GlobalContext.get().get<Context>()
        val resourceManager = GlobalContext.get().get<ResourceManager>()

        val intent = Intent(context, notification.targetActivity).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            notification.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(context, notification.channel.id)
            .setContentTitle(resourceManager.getString(notification.title))
            .setContentText(resourceManager.getString(notification.message))
            .setPriority(notification.priority.toNativePriority())
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
    }


    fun createGeneralNotificationsChannel() {
        val resourceManager = GlobalContext.get().get<ResourceManager>()

        val id = AppNotificationChannel.General.id
        val name = resourceManager.getString(R.string.general_notifications_channel_name)
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        val channel = NotificationChannel(id, name, importance)
        val manager = getNativeNotificationManager()

        manager.createNotificationChannel(channel)
    }

    fun checkNotificationPermission(): Boolean {
        val context = GlobalContext.get().get<Context>()
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else true
    }


    fun pushNotification(notificationType: AppNotificationType) {
        val appNotification = AppNotification.fromType(type = notificationType)
        val notificationManager = getNativeNotificationManager()
        val notification = setNotificationContent(notification = appNotification).build()

        notificationManager.notify(appNotification.id, notification)
    }

    fun tryPushNotification(notificationType: AppNotificationType) {
        if (!checkNotificationPermission()) return
        pushNotification(notificationType = notificationType)
    }

    fun cancelNotification(notificationType: AppNotificationType) {
        val appNotification = AppNotification.fromType(type = notificationType)
        val notificationManager = getNativeNotificationManager()

        notificationManager.cancel(appNotification.id)
    }


    fun scheduleNotification(notificationType: AppNotificationType) {
        val appNotification = AppNotification.fromType(type = notificationType)
        val context = GlobalContext.get().get<Context>()
        val data = Data.Builder()
            .putString("notificationType", appNotification.getType().name)
            .build()

        val (repeatInterval, repeatIntervalTimeUnit) = appNotification.getRepeatInterval() ?: return

        val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
            repeatInterval = repeatInterval,
            repeatIntervalTimeUnit = repeatIntervalTimeUnit
        )
            .addTag(tag = appNotification.tag)
            .run {
                appNotification.getDelayBeforeRepeatInterval()?.let { (duration, timeUnit) ->
                    setInitialDelay(duration = duration, timeUnit = timeUnit)
                } ?: this
            }
            .setInputData(inputData = data)
            .build()

        WorkManager.getInstance(context = context).enqueueUniquePeriodicWork(
            uniqueWorkName = appNotification.getUniquePeriodicWorkName(),
            existingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.UPDATE,
            request = workRequest
        )
    }

    fun cancelScheduledNotification(notificationType: AppNotificationType) {
        val appNotification = AppNotification.fromType(type = notificationType)
        val context = GlobalContext.get().get<Context>()

        WorkManager.getInstance(context = context).cancelAllWorkByTag(tag = appNotification.tag)
    }

}