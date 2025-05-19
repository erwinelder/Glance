package com.ataglance.walletglance.di

import com.ataglance.walletglance.settings.data.repository.SettingsRepository
import com.ataglance.walletglance.settings.domain.usecase.ChangeAppLookPreferencesUseCase
import com.ataglance.walletglance.settings.domain.usecase.ChangeAppLookPreferencesUseCaseImpl
import com.ataglance.walletglance.settings.domain.usecase.ChangeAppSetupStageUseCase
import com.ataglance.walletglance.settings.domain.usecase.ChangeAppSetupStageUseCaseImpl
import com.ataglance.walletglance.settings.domain.usecase.GetAppThemeConfigurationUseCase
import com.ataglance.walletglance.settings.domain.usecase.GetAppThemeConfigurationUseCaseImpl
import com.ataglance.walletglance.settings.domain.usecase.GetStartDestinationsBySetupStageUseCase
import com.ataglance.walletglance.settings.domain.usecase.GetStartDestinationsBySetupStageUseCaseImpl
import com.ataglance.walletglance.settings.domain.usecase.GetUserProfileLocalTimestampUseCase
import com.ataglance.walletglance.settings.domain.usecase.GetUserProfileLocalTimestampUseCaseImpl
import com.ataglance.walletglance.settings.domain.usecase.SaveUserProfileTimestampUseCase
import com.ataglance.walletglance.settings.domain.usecase.SaveUserProfileTimestampUseCaseImpl
import com.ataglance.walletglance.settings.domain.usecase.language.GetLanguagePreferenceUseCase
import com.ataglance.walletglance.settings.domain.usecase.language.GetLanguagePreferenceUseCaseImpl
import com.ataglance.walletglance.settings.domain.usecase.language.SaveLanguageLocallyUseCase
import com.ataglance.walletglance.settings.domain.usecase.language.SaveLanguageLocallyUseCaseImpl
import com.ataglance.walletglance.settings.domain.usecase.language.SaveLanguageRemotelyUseCase
import com.ataglance.walletglance.settings.domain.usecase.language.SaveLanguageRemotelyUseCaseImpl
import com.ataglance.walletglance.settings.domain.usecase.language.SaveLanguageToPreferencesUseCase
import com.ataglance.walletglance.settings.domain.usecase.language.SaveLanguageToPreferencesUseCaseImpl
import com.ataglance.walletglance.settings.domain.usecase.language.SaveLanguageUseCase
import com.ataglance.walletglance.settings.domain.usecase.language.SaveLanguageUseCaseImpl
import com.ataglance.walletglance.settings.presentation.viewmodel.LanguageViewModel
import com.ataglance.walletglance.settings.presentation.viewmodel.ResetDataViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {

    /* ---------- Repositories ---------- */

    single {
        SettingsRepository(dataStore = get())
    }

    /* ---------- Use Cases ---------- */

    single<GetUserProfileLocalTimestampUseCase> {
        GetUserProfileLocalTimestampUseCaseImpl(settingsRepository = get())
    }
    single<SaveUserProfileTimestampUseCase> {
        SaveUserProfileTimestampUseCaseImpl(settingsRepository = get())
    }

    single<GetLanguagePreferenceUseCase> {
        GetLanguagePreferenceUseCaseImpl(settingsRepository = get())
    }
    single<SaveLanguageToPreferencesUseCase> {
        SaveLanguageToPreferencesUseCaseImpl(settingsRepository = get())
    }
    single<SaveLanguageLocallyUseCase> {
        SaveLanguageLocallyUseCaseImpl(
            saveLanguageToPreferencesUseCase = get(),
            applyLanguageToSystemUseCase = get()
        )
    }
    single<SaveLanguageRemotelyUseCase> {
        SaveLanguageRemotelyUseCaseImpl(authRepository = get())
    }
    single<SaveLanguageUseCase> {
        SaveLanguageUseCaseImpl(
            saveLanguageLocallyUseCase = get(),
            saveLanguageRemotelyUseCase = get(),
            userContext = get()
        )
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

    /* ---------- View Models ---------- */

    viewModel { parameters ->
        LanguageViewModel(
            currentLangCode = parameters.get(),
            saveLanguageUseCase = get(),
            translateCategoriesUseCase = get()
        )
    }

    viewModel {
        ResetDataViewModel(
            deleteAllDataLocallyUseCase = get(),
            signOutUseCase = get()
        )
    }

}