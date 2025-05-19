package com.ataglance.walletglance.di

import com.ataglance.walletglance.core.data.local.database.AppDatabase
import com.ataglance.walletglance.core.data.local.preferences.SecureStorage
import com.ataglance.walletglance.core.data.model.DataSyncHelper
import com.ataglance.walletglance.core.data.model.SyncTablesContext
import com.ataglance.walletglance.core.domain.usecase.DeleteAllDataLocallyUseCase
import com.ataglance.walletglance.core.domain.usecase.DeleteAllDataLocallyUseCaseImpl
import com.ataglance.walletglance.core.presentation.viewmodel.AppViewModel
import com.ataglance.walletglance.core.presentation.viewmodel.ExpensesIncomeWidgetViewModel
import com.ataglance.walletglance.settings.domain.usecase.language.ApplyLanguageToSystemUseCase
import com.ataglance.walletglance.settings.domain.usecase.language.ApplyLanguageToSystemUseCaseImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val coreModule = module {

    /* ---------- Other ---------- */

    single {
        AppDatabase.getDatabase(context = get())
    }

    single {
        SecureStorage(settings = get())
    }

    single {
        SyncTablesContext()
    }

    single {
        DataSyncHelper(
            syncTablesContext = get(),
            userContext = get()
        )
    }

    factory {
        CoroutineScope(Dispatchers.IO)
    }

    /* ---------- Use Cases ---------- */

    single<ApplyLanguageToSystemUseCase> {
        ApplyLanguageToSystemUseCaseImpl()
    }

    single<DeleteAllDataLocallyUseCase> {
        DeleteAllDataLocallyUseCaseImpl(
            settingsRepository = get(),
            accountRepository = get(),
            categoryRepository = get(),
            categoryCollectionRepository = get(),
            widgetRepository = get(),
            navigationButtonRepository = get()
        )
    }

    /* ---------- View Models ---------- */

    viewModel {
        AppViewModel(
            checkTokenValidityUseCase = get(),

            getAppThemeConfigurationUseCase = get(),
            applyLanguageToSystemUseCase = get(),
            getLanguagePreferenceUseCase = get(),
            changeAppSetupStageUseCase = get(),
            getStartDestinationsBySetupStageUseCase = get(),

            getAccountsUseCase = get(),
            getWidgetsUseCase = get()
        )
    }

    viewModel { parameters ->
        ExpensesIncomeWidgetViewModel(
            activeAccount = parameters.getOrNull(),
            activeDateRange = parameters.get(),
            getRecordStacksInDateRangeUseCase = get()
        )
    }

}