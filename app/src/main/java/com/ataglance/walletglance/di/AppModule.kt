package com.ataglance.walletglance.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.ataglance.walletglance.core.data.local.database.AppDatabase
import com.ataglance.walletglance.core.data.remote.dao.RemoteUpdateTimeDao
import com.ataglance.walletglance.core.data.repository.GeneralRepository
import com.ataglance.walletglance.core.data.repository.SettingsRepository
import com.ataglance.walletglance.core.presentation.viewmodel.AppViewModel
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

val appModule = module {

    /* ---------- Other ---------- */

    single {
        AppDatabase.getDatabase(context = get())
    }

    single<DataStore<Preferences>> {
        androidContext().dataStore
    }

    single {
        FirebaseFirestore.getInstance()
    }

    /* ---------- DAOs ---------- */

    single {
        RemoteUpdateTimeDao(firestore = get())
    }

    /* ---------- Repositories ---------- */

    single {
        SettingsRepository(dataStore = get())
    }

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

    /* ---------- View Models ---------- */

    viewModel {
        AppViewModel(
            settingsRepository = get(),

            saveAccountsUseCase = get(),
            getAccountsUseCase = get(),

            saveCategoriesUseCase = get(),
            getAllCategoriesUseCase = get(),

            saveCategoryCollectionsUseCase = get(),
            getCategoryCollectionsUseCase = get(),

            recordRepository = get(),
            getLastRecordNumUseCase = get(),
            getTodayTotalExpensesForAccountUseCase = get(),
            getRecordStacksInDateRangeUseCase = get(),

            saveBudgetsUseCase = get(),
            getBudgetsUseCase = get(),

            generalRepository = get()
        )
    }

}