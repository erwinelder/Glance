package com.ataglance.walletglance.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            appModule, authModule, billingModule,
            accountModule, categoryModule, recordModule, budgetModule,
            repositoryModule
        )
    }
}