package com.ataglance.walletglance

import android.app.Application
import com.ataglance.walletglance.di.appModule
import com.ataglance.walletglance.di.repositoryModule
import com.ataglance.walletglance.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class GlanceApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@GlanceApplication)
            modules(appModule, repositoryModule, viewModelModule)
        }

    }

}
