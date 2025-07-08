package com.ataglance.walletglance.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            coreModule, corePlatformModule,
            settingsModule, settingsPlatformModule,
            authModule,
            billingModule,
            accountModule, categoryModule,
            transactionModule, recordModule, transferModule,
            categoryCollectionModule, budgetModule,
            navigationModule, personalizationModule
        )
    }
}