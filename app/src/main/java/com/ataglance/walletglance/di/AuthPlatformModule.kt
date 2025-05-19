package com.ataglance.walletglance.di

import com.ataglance.walletglance.auth.data.repository.UserFirestoreRepository
import com.ataglance.walletglance.auth.data.repository.UserRepository
import org.koin.dsl.module

val authPlatformModule = module {

    /* ---------- Repositories ---------- */

    single<UserRepository> {
        UserFirestoreRepository(firestore = get())
    }

}