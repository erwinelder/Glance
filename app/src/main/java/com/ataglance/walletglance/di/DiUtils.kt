package com.ataglance.walletglance.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module


fun initializeKoinMockedModule(
    moduleDeclaration: Module.() -> Unit
) {
    startKoin {
        modules(
            module {
                moduleDeclaration()
            }
        )
    }
}