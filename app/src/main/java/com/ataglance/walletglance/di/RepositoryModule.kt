package com.ataglance.walletglance.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.ataglance.walletglance.auth.domain.model.AuthController
import com.ataglance.walletglance.core.data.repository.GeneralRepository
import com.ataglance.walletglance.core.data.repository.RepositoryFactory
import com.ataglance.walletglance.core.data.repository.SettingsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

val repositoryModule = module {

    single<DataStore<Preferences>> { androidContext().dataStore }
    single { SettingsRepository(dataStore = get()) }

    scope(named("userSession")) {

        scoped {
            RepositoryFactory(db = get(), user = get<AuthController>().getUser(), firestore = get())
        }

        scoped { get<RepositoryFactory>().getNavigationButtonRepository() }
        scoped { get<RepositoryFactory>().getWidgetRepository() }
        scoped { get<RepositoryFactory>().getBudgetOnWidgetRepository() }
        scoped {
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

}