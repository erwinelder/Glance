package com.ataglance.walletglance.di

import com.ataglance.walletglance.billing.domain.model.BillingSubscriptionManager
import com.ataglance.walletglance.billing.domain.usecase.UpdateUserSubscriptionUseCase
import com.ataglance.walletglance.billing.domain.usecase.UpdateUserSubscriptionUseCaseImpl
import com.ataglance.walletglance.billing.presentation.viewmodel.SubscriptionViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val billingModule = module {

    /* ---------- Other ---------- */

    single {
        BillingSubscriptionManager(
            context = get(),
            coroutineScope = get(),
            userContext = get(),
            updateUserSubscriptionUseCase = get()
        )
    }

    /* ---------- Use Cases ---------- */

    single<UpdateUserSubscriptionUseCase> {
        UpdateUserSubscriptionUseCaseImpl(userRepository = get())
    }

    /* ---------- View Models ---------- */

    viewModel {
        SubscriptionViewModel(billingSubscriptionManager = get())
    }

}