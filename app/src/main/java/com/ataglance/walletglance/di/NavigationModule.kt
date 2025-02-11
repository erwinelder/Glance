package com.ataglance.walletglance.di

import com.ataglance.walletglance.core.data.remote.FirestoreAdapterFactory
import com.ataglance.walletglance.navigation.data.local.source.NavigationButtonLocalDataSource
import com.ataglance.walletglance.navigation.data.local.source.getNavigationButtonLocalDataSource
import com.ataglance.walletglance.navigation.data.remote.dao.NavigationButtonRemoteDao
import com.ataglance.walletglance.navigation.data.remote.source.NavigationButtonRemoteDataSource
import com.ataglance.walletglance.navigation.data.remote.source.NavigationButtonRemoteDataSourceImpl
import com.ataglance.walletglance.navigation.data.repository.NavigationButtonRepository
import com.ataglance.walletglance.navigation.data.repository.NavigationButtonRepositoryImpl
import com.ataglance.walletglance.navigation.domain.usecase.GetNavigationButtonsUseCase
import com.ataglance.walletglance.navigation.domain.usecase.GetNavigationButtonsUseCaseImpl
import com.ataglance.walletglance.navigation.domain.usecase.SaveNavigationButtonsUseCase
import com.ataglance.walletglance.navigation.domain.usecase.SaveNavigationButtonsUseCaseImpl
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val navigationModule = module {

    /* ---------- DAOs ---------- */

    single {
        NavigationButtonRemoteDao(
            firestoreAdapter = FirestoreAdapterFactory(firestore = get())
                .getNavigationButtonFirestoreAdapter()
        )
    }

    /* ---------- Data Sources ---------- */

    single<NavigationButtonLocalDataSource> {
        getNavigationButtonLocalDataSource(appDatabase = get())
    }

    single<NavigationButtonRemoteDataSource> {
        NavigationButtonRemoteDataSourceImpl(navigationButtonDao = get(), updateTimeDao = get())
    }

    /* ---------- Repositories ---------- */

    single<NavigationButtonRepository> {
        NavigationButtonRepositoryImpl(
            localSource = get(), remoteSource = get(), userContext = get()
        )
    }

    /* ---------- Use Cases ---------- */

    single<SaveNavigationButtonsUseCase> {
        SaveNavigationButtonsUseCaseImpl(navigationButtonRepository = get())
    }

    single<GetNavigationButtonsUseCase> {
        GetNavigationButtonsUseCaseImpl(navigationButtonRepository = get())
    }

    /* ---------- View Models ---------- */

    viewModel {
        NavigationViewModel(
            saveNavigationButtonsUseCase = get(),
            getNavigationButtonsUseCase = get()
        )
    }

}