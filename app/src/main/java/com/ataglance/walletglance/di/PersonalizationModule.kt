package com.ataglance.walletglance.di

import com.ataglance.walletglance.core.data.remote.FirestoreAdapterFactory
import com.ataglance.walletglance.personalization.data.local.source.WidgetLocalDataSource
import com.ataglance.walletglance.personalization.data.local.source.getWidgetLocalDataSource
import com.ataglance.walletglance.personalization.data.remote.dao.WidgetRemoteDao
import com.ataglance.walletglance.personalization.data.remote.source.WidgetRemoteDataSource
import com.ataglance.walletglance.personalization.data.remote.source.WidgetRemoteDataSourceImpl
import com.ataglance.walletglance.personalization.data.repository.WidgetRepository
import com.ataglance.walletglance.personalization.data.repository.WidgetRepositoryImpl
import com.ataglance.walletglance.personalization.domain.usecase.widgets.GetWidgetsUseCase
import com.ataglance.walletglance.personalization.domain.usecase.widgets.GetWidgetsUseCaseImpl
import com.ataglance.walletglance.personalization.domain.usecase.widgets.SaveWidgetsUseCase
import com.ataglance.walletglance.personalization.domain.usecase.widgets.SaveWidgetsUseCaseImpl
import com.ataglance.walletglance.personalization.presentation.viewmodel.PersonalizationViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val personalizationModule = module {

    /* ---------- DAOs ---------- */

    single {
        WidgetRemoteDao(
            firestoreAdapter = get<FirestoreAdapterFactory>().getWidgetFirestoreAdapter()
        )
    }

    /* ---------- Data Sources ---------- */

    single<WidgetLocalDataSource> {
        getWidgetLocalDataSource(appDatabase = get())
    }

    single<WidgetRemoteDataSource> {
        WidgetRemoteDataSourceImpl(widgetDao = get(), updateTimeDao = get())
    }

    /* ---------- Repositories ---------- */

    single<WidgetRepository> {
        WidgetRepositoryImpl(localSource = get(), remoteSource = get(), syncHelper = get())
    }

    /* ---------- Use Cases ---------- */

    single<SaveWidgetsUseCase> {
        SaveWidgetsUseCaseImpl(widgetRepository = get())
    }

    single<GetWidgetsUseCase> {
        GetWidgetsUseCaseImpl(widgetRepository = get())
    }

    /* ---------- View Models ---------- */

    viewModel {
        PersonalizationViewModel(
            getAppThemeConfigurationUseCase = get(),
            changeAppLookPreferencesUseCase = get(),
            saveWidgetsUseCase = get(),
            getWidgetsUseCase = get(),
            saveNavigationButtonsUseCase = get(),
            getNavigationButtonsUseCase = get(),
            userContext = get()
        )
    }

}