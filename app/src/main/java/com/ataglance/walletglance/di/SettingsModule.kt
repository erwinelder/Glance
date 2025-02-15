package com.ataglance.walletglance.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.ataglance.walletglance.settings.data.repository.SettingsRepository
import com.ataglance.walletglance.settings.domain.usecase.ChangeAppLookPreferencesUseCase
import com.ataglance.walletglance.settings.domain.usecase.ChangeAppLookPreferencesUseCaseImpl
import com.ataglance.walletglance.settings.domain.usecase.ChangeAppSetupStageUseCase
import com.ataglance.walletglance.settings.domain.usecase.ChangeAppSetupStageUseCaseImpl
import com.ataglance.walletglance.settings.domain.usecase.GetAppThemeConfigurationUseCase
import com.ataglance.walletglance.settings.domain.usecase.GetAppThemeConfigurationUseCaseImpl
import com.ataglance.walletglance.settings.domain.usecase.GetLanguagePreferenceUseCase
import com.ataglance.walletglance.settings.domain.usecase.GetLanguagePreferenceUseCaseImpl
import com.ataglance.walletglance.settings.domain.usecase.GetStartDestinationsBySetupStageUseCase
import com.ataglance.walletglance.settings.domain.usecase.GetStartDestinationsBySetupStageUseCaseImpl
import com.ataglance.walletglance.settings.domain.usecase.GetUserIdPreferenceUseCase
import com.ataglance.walletglance.settings.domain.usecase.GetUserIdPreferenceUseCaseImpl
import com.ataglance.walletglance.settings.domain.usecase.SaveLanguagePreferenceUseCase
import com.ataglance.walletglance.settings.domain.usecase.SaveLanguagePreferenceUseCaseImpl
import com.ataglance.walletglance.settings.domain.usecase.SaveUserIdPreferenceUseCase
import com.ataglance.walletglance.settings.domain.usecase.SaveUserIdPreferenceUseCaseImpl
import com.ataglance.walletglance.settings.presentation.viewmodel.LanguageViewModel
import com.ataglance.walletglance.settings.presentation.viewmodel.ResetDataViewModel
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

    single<ChangeAppSetupStageUseCase> {
        ChangeAppSetupStageUseCaseImpl(settingsRepository = get())
    }

    single<GetStartDestinationsBySetupStageUseCase> {
        GetStartDestinationsBySetupStageUseCaseImpl(settingsRepository = get())
    }

    single<SaveUserIdPreferenceUseCase> {
        SaveUserIdPreferenceUseCaseImpl(settingsRepository = get())
    }

    single<GetUserIdPreferenceUseCase> {
        GetUserIdPreferenceUseCaseImpl(settingsRepository = get())
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

    viewModel {
        ResetDataViewModel(
            deleteAllDataLocallyUseCase = get(),
            authController = get()
        )
    }

}