package com.ataglance.walletglance.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.ataglance.walletglance.auth.data.repository.UserRepository
import com.ataglance.walletglance.auth.data.repository.UserRepositoryImpl
import com.ataglance.walletglance.auth.domain.model.AuthController
import com.ataglance.walletglance.core.data.preferences.SettingsRepository
import com.ataglance.walletglance.core.data.repository.GeneralRepository
import com.ataglance.walletglance.core.data.repository.RepositoryFactory
import com.ataglance.walletglance.recordAndAccount.data.repository.RecordAndAccountRepository
import com.ataglance.walletglance.recordAndAccount.data.repository.RecordAndAccountRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

val repositoryModule = module {
    single { androidContext().dataStore }
    single { SettingsRepository(dataStore = get()) }

    single<UserRepository> { UserRepositoryImpl(firestore = get()) }

    single { RepositoryFactory(db = get(), user = get<AuthController>().user, firestore = get()) }

    single { get<RepositoryFactory>().getNavigationButtonRepository() }
    single { get<RepositoryFactory>().getWidgetRepository() }
    single { get<RepositoryFactory>().getBudgetOnWidgetRepository() }
    single { get<RepositoryFactory>().getAccountRepository() }
    single { get<RepositoryFactory>().getCategoryRepository() }
    single { get<RepositoryFactory>().getCategoryCollectionRepository() }
    single {
        get<RepositoryFactory>().getCategoryCollectionAndCollectionCategoryAssociationRepository()
    }
    single { get<RepositoryFactory>().getRecordRepository() }
    single { get<RepositoryFactory>().getBudgetAndBudgetAccountAssociationRepository() }
    single<RecordAndAccountRepository> {
        RecordAndAccountRepositoryImpl(recordRepository = get(), accountRepository = get())
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
}