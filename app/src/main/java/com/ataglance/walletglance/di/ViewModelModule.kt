package com.ataglance.walletglance.di

import com.ataglance.walletglance.core.presentation.viewmodel.AppViewModel
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.personalization.presentation.viewmodel.PersonalizationViewModel
import org.koin.dsl.module

val viewModelModule = module {
    single {
        AppViewModel(
            settingsRepository = get(),
            accountRepository = get(),
            categoryRepository = get(),
            categoryCollectionAndAssociationRepository = get(),
            recordRepository = get(),
            recordAndAccountRepository = get(),
            budgetAndAssociationRepository = get(),
            generalRepository = get()
        )
    }
    single { NavigationViewModel(navigationButtonRepository = get()) }
    single { PersonalizationViewModel(widgetRepository = get(), budgetOnWidgetRepository = get()) }
}