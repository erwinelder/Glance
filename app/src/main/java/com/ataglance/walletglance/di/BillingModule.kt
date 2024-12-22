package com.ataglance.walletglance.di

import com.ataglance.walletglance.auth.domain.model.AuthController
import com.ataglance.walletglance.billing.domain.model.BillingSubscriptionManager
import com.ataglance.walletglance.billing.domain.usecase.UpdateUserSubscriptionUseCase
import com.ataglance.walletglance.billing.domain.usecase.UpdateUserSubscriptionUseCaseImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

val billingModule = module {

    scope(named("userSession")) {

        scoped {
            BillingSubscriptionManager(
                context = get(),
                coroutineScope = CoroutineScope(Dispatchers.IO),
                user = get<AuthController>().getUser(),
                updateUserSubscriptionUseCase = get()
            )
        }

    }

    single<UpdateUserSubscriptionUseCase> {
        UpdateUserSubscriptionUseCaseImpl(userRepository = get())
    }

}