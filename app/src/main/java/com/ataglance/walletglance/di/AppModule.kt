package com.ataglance.walletglance.di

import com.ataglance.walletglance.core.data.local.database.AppDatabase
import com.ataglance.walletglance.core.data.remote.dao.RemoteUpdateTimeDao
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.dsl.module

val appModule = module {

    /* ---------- DAOs ---------- */

    single {
        RemoteUpdateTimeDao(firestore = get())
    }

    /* ---------- Other ---------- */

    single {
        AppDatabase.getDatabase(context = get())
    }

    single {
        FirebaseFirestore.getInstance()
    }

}