package com.ataglance.walletglance.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.ataglance.walletglance.core.data.repository.SettingsRepository
import com.ataglance.walletglance.settings.domain.usecase.ChangeAppLookPreferencesUseCase
import com.ataglance.walletglance.settings.domain.usecase.ChangeAppLookPreferencesUseCaseImpl
import com.ataglance.walletglance.settings.domain.usecase.GetAppThemeConfigurationUseCase
import com.ataglance.walletglance.settings.domain.usecase.GetAppThemeConfigurationUseCaseImpl
import com.ataglance.walletglance.settings.domain.usecase.GetLanguagePreferenceUseCase
import com.ataglance.walletglance.settings.domain.usecase.GetLanguagePreferenceUseCaseImpl
import com.ataglance.walletglance.settings.domain.usecase.SaveLanguagePreferenceUseCase
import com.ataglance.walletglance.settings.domain.usecase.SaveLanguagePreferenceUseCaseImpl
import com.ataglance.walletglance.settings.presentation.viewmodel.LanguageViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

val settingsModule = module {

    /* ---------- Other ---------- */

    single<DataStore<Preferences>> {
        androidContext().dataStore
    }

    /* ---------- Repositories ---------- */

    single {
        SettingsRepository(dataStore = get())
    }

    /* ---------- Use Cases ---------- */

    single<SaveLanguagePreferenceUseCase> {
        SaveLanguagePreferenceUseCaseImpl(settingsRepository = get())
    }

    single<GetLanguagePreferenceUseCase> {
        GetLanguagePreferenceUseCaseImpl(settingsRepository = get())
    }

    single<GetAppThemeConfigurationUseCase> {
        GetAppThemeConfigurationUseCaseImpl(settingsRepository = get())
    }

    single<ChangeAppLookPreferencesUseCase> {
        ChangeAppLookPreferencesUseCaseImpl(settingsRepository = get())
    }

    /* ---------- View Models ---------- */

    viewModel { parameters ->
        LanguageViewModel(
            currentLangCode = parameters.get(),
            applyLanguageToSystemUseCase = get(),
            saveLanguagePreferenceUseCase = get(),
            translateCategoriesUseCase = get()
        )
    }

}