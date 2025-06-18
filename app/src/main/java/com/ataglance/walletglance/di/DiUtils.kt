package com.ataglance.walletglance.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initializeKoinMockedModule(
    config: KoinAppDeclaration? = null,
    moduleDeclaration: Module.() -> Unit
) {
    startKoin {
        config?.invoke(this)
        modules(
            module {
                moduleDeclaration()
            }
        )
    }
}