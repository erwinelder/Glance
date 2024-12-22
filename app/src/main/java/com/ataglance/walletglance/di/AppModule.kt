package com.ataglance.walletglance.di

import com.ataglance.walletglance.auth.domain.model.AuthController
import com.ataglance.walletglance.billing.domain.model.BillingSubscriptionManager
import com.ataglance.walletglance.core.data.local.AppDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {

    single { AppDatabase.getDatabase(context = get()) }
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { AuthController(auth = get(), userRepository = get()) }

    scope(named("userSession")) {

        scoped {
            BillingSubscriptionManager(
                context = get(),
                coroutineScope = CoroutineScope(Dispatchers.IO),
                user = get<AuthController>().getUser(),
                userRepository = get()
            )
        }

    }

}