package com.ataglance.walletglance.di

import com.ataglance.walletglance.navigation.data.local.source.NavigationButtonLocalDataSource
import com.ataglance.walletglance.navigation.data.local.source.getNavigationButtonLocalDataSource
import com.ataglance.walletglance.navigation.data.remote.source.NavigationButtonRemoteDataSource
import com.ataglance.walletglance.navigation.data.remote.source.NavigationButtonRemoteDataSourceImpl
import com.ataglance.walletglance.navigation.data.repository.NavigationButtonRepository
import com.ataglance.walletglance.navigation.data.repository.NavigationButtonRepositoryImpl
import com.ataglance.walletglance.navigation.domain.usecase.GetNavigationButtonScreensUseCase
import com.ataglance.walletglance.navigation.domain.usecase.GetNavigationButtonScreensUseCaseImpl
import com.ataglance.walletglance.navigation.domain.usecase.SaveNavigationButtonsUseCase
import com.ataglance.walletglance.navigation.domain.usecase.SaveNavigationButtonsUseCaseImpl
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val navigationModule = module {

    /* ---------- Data Sources ---------- */

    single<NavigationButtonLocalDataSource> {
        getNavigationButtonLocalDataSource(appDatabase = get())
    }

    single<NavigationButtonRemoteDataSource> {
        NavigationButtonRemoteDataSourceImpl()
    }

    /* ---------- Repositories ---------- */

    single<NavigationButtonRepository> {
        NavigationButtonRepositoryImpl(
            localSource = get(),
            remoteSource = get(),
            syncHelper = get()
        )
    }

    /* ---------- Use Cases ---------- */

    single<SaveNavigationButtonsUseCase> {
        SaveNavigationButtonsUseCaseImpl(navigationButtonRepository = get())
    }

    single<GetNavigationButtonScreensUseCase> {
        GetNavigationButtonScreensUseCaseImpl(navigationButtonRepository = get())
    }

    /* ---------- ViewModels ---------- */

    viewModel {
        NavigationViewModel(getNavigationButtonScreensUseCase = get())
    }

}