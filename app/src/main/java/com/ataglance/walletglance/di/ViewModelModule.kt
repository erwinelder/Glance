package com.ataglance.walletglance.di

import com.ataglance.walletglance.core.presentation.viewmodel.AppViewModel
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.personalization.presentation.viewmodel.PersonalizationViewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val viewModelModule = module {
    scope(named("UserScope")) {
        scoped {
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
        scoped { NavigationViewModel(navigationButtonRepository = get()) }
        scoped {
            PersonalizationViewModel(widgetRepository = get(), budgetOnWidgetRepository = get())
        }
    }
}