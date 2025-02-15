package com.ataglance.walletglance.di

import android.content.Context
import com.ataglance.walletglance.core.data.local.database.AppDatabase
import com.ataglance.walletglance.core.data.remote.dao.RemoteUpdateTimeDao
import com.ataglance.walletglance.core.domain.usecase.DeleteAllDataLocallyUseCase
import com.ataglance.walletglance.core.domain.usecase.DeleteAllDataLocallyUseCaseImpl
import com.ataglance.walletglance.core.presentation.model.ResourceManager
import com.ataglance.walletglance.core.presentation.model.ResourceManagerImpl
import com.ataglance.walletglance.core.presentation.viewmodel.AppViewModel
import com.ataglance.walletglance.core.presentation.viewmodel.ExpensesIncomeWidgetViewModel
import com.ataglance.walletglance.settings.domain.usecase.ApplyLanguageToSystemUseCase
import com.ataglance.walletglance.settings.domain.usecase.ApplyLanguageToSystemUseCaseImpl
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import java.util.Locale

val appModule = module {

    /* ---------- Other ---------- */

    single {
        AppDatabase.getDatabase(context = get())
    }

    single {
        FirebaseFirestore.getInstance()
    }

    single<ResourceManager> { parameters ->
        val locale = parameters.get<String>()
        val context = get<Context>().let {
            it.createConfigurationContext(
                it.resources.configuration.apply { setLocale(Locale(locale)) }
            )
        }
        ResourceManagerImpl(context = context)
    }

    /* ---------- DAOs ---------- */

    single {
        RemoteUpdateTimeDao(firestore = get())
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
            settingsRepository = get(),
            getAppThemeConfigurationUseCase = get(),
            applyLanguageToSystemUseCase = get(),
            saveLanguagePreferenceUseCase = get(),
            getLanguagePreferenceUseCase = get(),
            changeAppSetupStatusUseCase = get(),

            getAccountsUseCase = get(),
            getWidgetsUseCase = get(),
            deleteAllDataLocallyUseCase = get()
        )
    }

    viewModel { parameters ->
        ExpensesIncomeWidgetViewModel(
            activeAccount = parameters.get(),
            activeDateRange = parameters.get(),
            getRecordStacksInDateRangeUseCase = get()
        )
    }

}