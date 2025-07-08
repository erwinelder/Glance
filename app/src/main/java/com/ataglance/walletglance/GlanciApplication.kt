package com.ataglance.walletglance

import android.app.Application
import com.ataglance.walletglance.di.initKoin
import com.ataglance.walletglance.notification.domain.model.NotificationManager
import org.koin.android.ext.koin.androidContext

class GlanciApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@GlanciApplication)
        }

        NotificationManager.createGeneralNotificationsChannel()
    }

}
