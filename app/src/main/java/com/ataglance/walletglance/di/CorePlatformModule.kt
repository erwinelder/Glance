package com.ataglance.walletglance.di

import android.content.Context
import com.ataglance.walletglance.core.data.local.preferences.SecureStorageWithKeyGenerator
import com.ataglance.walletglance.core.presentation.model.ResourceManager
import com.ataglance.walletglance.core.presentation.model.ResourceManagerImpl
import com.ataglance.walletglance.core.presentation.vibration.Vibrator
import com.ataglance.walletglance.core.presentation.vibration.VibratorImpl
import com.google.firebase.firestore.FirebaseFirestore
import com.russhwolf.settings.Settings
import org.koin.dsl.module
import java.util.Locale

val corePlatformModule = module {

    /* ---------- Other ---------- */

    single<Settings> {
        SecureStorageWithKeyGenerator(context = get())
    }

    single {
        FirebaseFirestore.getInstance()
    }

    factory<ResourceManager> { parameters ->
        val locale = parameters.getOrNull<String>()

        val context = get<Context>().let { context ->
            locale?.let {
                context.createConfigurationContext(
                    context.resources.configuration.apply {
                        setLocale(Locale.forLanguageTag(locale))
                    }
                )
            } ?: context
        }

        ResourceManagerImpl(context = context)
    }

    /* ---------- Vibration ---------- */

    single<Vibrator> {
        VibratorImpl(context = get())
    }

}