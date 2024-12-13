package com.ataglance.walletglance.di

import com.ataglance.walletglance.auth.domain.model.AuthController
import com.ataglance.walletglance.billing.domain.model.BillingManager
import com.ataglance.walletglance.core.data.local.AppDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val appModule = module {
    factory { CoroutineScope(Dispatchers.IO) }

    single { AppDatabase.getDatabase(get()) }
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { AuthController(auth = get(), userRepository = get()) }
    single { BillingManager(context = get(), coroutineScope = get(), userRepository = get()) }
}