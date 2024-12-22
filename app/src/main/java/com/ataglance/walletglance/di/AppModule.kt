package com.ataglance.walletglance.di

import com.ataglance.walletglance.core.data.local.AppDatabase
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.dsl.module

val appModule = module {

    single { AppDatabase.getDatabase(context = get()) }
    single { FirebaseFirestore.getInstance() }

}