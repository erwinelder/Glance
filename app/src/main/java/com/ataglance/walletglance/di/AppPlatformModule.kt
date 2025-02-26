package com.ataglance.walletglance.di

import android.content.Context
import com.ataglance.walletglance.core.data.remote.FirestoreAdapterFactory
import com.ataglance.walletglance.core.data.remote.FirestoreAdapterFactoryImpl
import com.ataglance.walletglance.core.data.remote.dao.RemoteUpdateTimeDao
import com.ataglance.walletglance.core.presentation.model.ResourceManager
import com.ataglance.walletglance.core.presentation.model.ResourceManagerImpl
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.dsl.module
import java.util.Locale

val appPlatformModule = module {

    /* ---------- Other ---------- */

    single {
        FirebaseFirestore.getInstance()
    }

    factory<FirestoreAdapterFactory> {
        FirestoreAdapterFactoryImpl(firestore = get())
    }

    factory<ResourceManager> { parameters ->
        val locale = parameters.getOrNull<String>()

        val context = get<Context>().let { context ->
            locale?.let {
                context.createConfigurationContext(
                    context.resources.configuration.apply { setLocale(Locale(locale)) }
                )
            } ?: context
        }

        ResourceManagerImpl(context = context)
    }

    /* ---------- DAOs ---------- */

    single {
        RemoteUpdateTimeDao(firestore = get())
    }

}