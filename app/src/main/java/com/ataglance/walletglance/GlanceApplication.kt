package com.ataglance.walletglance

import android.app.Application
import com.ataglance.walletglance.di.initKoin
import org.koin.android.ext.koin.androidContext

class GlanceApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@GlanceApplication)
        }
    }

}
