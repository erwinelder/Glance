package com.ataglance.walletglance.di

import android.content.Context
import com.ataglance.walletglance.core.data.local.database.AppDatabase
import com.ataglance.walletglance.core.data.remote.dao.RemoteUpdateTimeDao
import com.ataglance.walletglance.core.data.repository.GeneralRepository
import com.ataglance.walletglance.core.presentation.model.ResourceManager
import com.ataglance.walletglance.core.presentation.model.ResourceManagerImpl
import com.ataglance.walletglance.core.presentation.viewmodel.AppViewModel
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

    /* ---------- Repositories ---------- */

    single {
        GeneralRepository(
            settingsRepository = get(),
            accountRepository = get(),
            categoryRepository = get(),
            categoryCollectionRepository = get(),
            widgetRepository = get(),
            navigationButtonRepository = get()
        )
    }

    /* ---------- Use Cases ---------- */

    single<ApplyLanguageToSystemUseCase> {
        ApplyLanguageToSystemUseCaseImpl()
    }

    /* ---------- View Models ---------- */

    viewModel {
        AppViewModel(
            settingsRepository = get(),
            getAppThemeConfigurationUseCase = get(),
            applyLanguageToSystemUseCase = get(),
            saveLanguagePreferenceUseCase = get(),
            getLanguagePreferenceUseCase = get(),

            getAccountsUseCase = get(),

            recordRepository = get(),
            getTodayTotalExpensesForAccountUseCase = get(),
            getRecordStacksInDateRangeUseCase = get(),

            getWidgetsUseCase = get(),

            generalRepository = get()
        )
    }

}