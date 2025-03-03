package com.ataglance.walletglance.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            appModule, appPlatformModule,
            authModule, authPlatformModule,
            billingModule, settingsModule, settingsPlatformModule,
            accountModule, categoryModule, recordModule, recordCreationModule,
            categoryCollectionModule, budgetModule,
            navigationModule, personalizationModule
        )
    }
}