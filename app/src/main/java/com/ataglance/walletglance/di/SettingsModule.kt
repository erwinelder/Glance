package com.ataglance.walletglance.di

import com.ataglance.walletglance.auth.domain.usecase.user.GetUserProfileLocalTimestampUseCase
import com.ataglance.walletglance.auth.domain.usecase.user.GetUserProfileLocalTimestampUseCaseImpl
import com.ataglance.walletglance.auth.domain.usecase.user.SaveUserProfileTimestampUseCase
import com.ataglance.walletglance.auth.domain.usecase.user.SaveUserProfileTimestampUseCaseImpl
import com.ataglance.walletglance.notification.domain.usecase.GetTurnOnDailyRecordsReminderPreferenceUseCase
import com.ataglance.walletglance.notification.domain.usecase.GetTurnOnDailyRecordsReminderPreferenceUseCaseImpl
import com.ataglance.walletglance.notification.domain.usecase.SaveTurnOnDailyRecordsReminderPreferenceUseCase
import com.ataglance.walletglance.notification.domain.usecase.SaveTurnOnDailyRecordsReminderPreferenceUseCaseImpl
import com.ataglance.walletglance.notification.presentation.viewmodel.NotificationsViewModel
import com.ataglance.walletglance.personalization.domain.usecase.theme.ChangeAppLookPreferencesUseCase
import com.ataglance.walletglance.personalization.domain.usecase.theme.ChangeAppLookPreferencesUseCaseImpl
import com.ataglance.walletglance.personalization.domain.usecase.theme.GetAppThemeConfigurationUseCase
import com.ataglance.walletglance.personalization.domain.usecase.theme.GetAppThemeConfigurationUseCaseImpl
import com.ataglance.walletglance.settings.data.repository.SettingsRepository
import com.ataglance.walletglance.settings.domain.usecase.ChangeAppSetupStageUseCase
import com.ataglance.walletglance.settings.domain.usecase.ChangeAppSetupStageUseCaseImpl
import com.ataglance.walletglance.settings.domain.usecase.GetStartDestinationsBySetupStageUseCase
import com.ataglance.walletglance.settings.domain.usecase.GetStartDestinationsBySetupStageUseCaseImpl
import com.ataglance.walletglance.settings.domain.usecase.language.GetLanguagePreferenceUseCase
import com.ataglance.walletglance.settings.domain.usecase.language.GetLanguagePreferenceUseCaseImpl
import com.ataglance.walletglance.settings.domain.usecase.language.SaveLanguageLocallyUseCase
import com.ataglance.walletglance.settings.domain.usecase.language.SaveLanguageLocallyUseCaseImpl
import com.ataglance.walletglance.settings.domain.usecase.language.SaveLanguageRemotelyUseCase
import com.ataglance.walletglance.settings.domain.usecase.language.SaveLanguageRemotelyUseCaseImpl
import com.ataglance.walletglance.settings.domain.usecase.language.SaveLanguageToPreferencesUseCase
import com.ataglance.walletglance.settings.domain.usecase.language.SaveLanguageToPreferencesUseCaseImpl
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

    single<GetTurnOnDailyRecordsReminderPreferenceUseCase> {
        GetTurnOnDailyRecordsReminderPreferenceUseCaseImpl(settingsRepository = get())
    }
    single<SaveTurnOnDailyRecordsReminderPreferenceUseCase> {
        SaveTurnOnDailyRecordsReminderPreferenceUseCaseImpl(settingsRepository = get())
    }

    /* ---------- ViewModels ---------- */

    viewModel { parameters ->
        LanguageViewModel(
            currentLangCode = parameters.get(),
            translateCategoriesUseCase = get(),
            saveLanguageLocallyUseCase = get(),
            saveLanguageRemotelyUseCase = get(),
            userContext = get()
        )
    }

    viewModel {
        ResetDataViewModel(
            deleteAllDataLocallyUseCase = get(),
            signOutUseCase = get()
        )
    }

    viewModel {
        NotificationsViewModel(
            getTurnOnDailyRecordsReminderPreferenceUseCase = get(),
            saveTurnOnDailyRecordsReminderPreferenceUseCase = get()
        )
    }

}