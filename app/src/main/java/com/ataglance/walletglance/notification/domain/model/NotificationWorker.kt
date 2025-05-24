package com.ataglance.walletglance.notification.domain.model

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.ataglance.walletglance.core.utils.enumValueOrNull

class NotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context = context, workerParams = workerParams) {

    override fun doWork(): Result {
        val notificationType = inputData.getString("notificationType")
            ?.let { enumValueOrNull<AppNotificationType>(name = it) }
            ?: return Result.failure()

        NotificationManager.tryPushNotification(notificationType = notificationType)

        return Result.success()
    }

}