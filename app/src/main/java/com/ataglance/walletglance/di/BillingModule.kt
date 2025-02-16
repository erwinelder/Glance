package com.ataglance.walletglance.di

import com.ataglance.walletglance.billing.domain.model.BillingSubscriptionManager
import com.ataglance.walletglance.billing.domain.usecase.UpdateUserSubscriptionUseCase
import com.ataglance.walletglance.billing.domain.usecase.UpdateUserSubscriptionUseCaseImpl
import com.ataglance.walletglance.billing.presentation.viewmodel.SubscriptionViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val billingModule = module {

    /* ---------- Other ---------- */

    single {
        BillingSubscriptionManager(
            context = get(),
            coroutineScope = CoroutineScope(Dispatchers.IO),
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
        SubscriptionViewModel(
            billingSubscriptionManager = get()
        )
    }

}