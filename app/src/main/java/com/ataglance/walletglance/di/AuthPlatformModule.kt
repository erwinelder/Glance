package com.ataglance.walletglance.di

import com.ataglance.walletglance.auth.data.repository.UserFirestoreRepository
import com.ataglance.walletglance.auth.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import org.koin.dsl.module

val authPlatformModule = module {

    /* ---------- Other ---------- */

    single {
        FirebaseAuth.getInstance()
    }

    /* ---------- Repositories ---------- */

    single<UserRepository> {
        UserFirestoreRepository(firestore = get())
    }

}