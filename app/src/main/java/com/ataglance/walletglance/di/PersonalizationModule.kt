package com.ataglance.walletglance.di

import com.ataglance.walletglance.core.data.remote.FirestoreAdapterFactory
import com.ataglance.walletglance.personalization.data.local.source.BudgetOnWidgetLocalDataSource
import com.ataglance.walletglance.personalization.data.local.source.WidgetLocalDataSource
import com.ataglance.walletglance.personalization.data.local.source.getBudgetOnWidgetLocalDataSource
import com.ataglance.walletglance.personalization.data.local.source.getWidgetLocalDataSource
import com.ataglance.walletglance.personalization.data.remote.dao.BudgetOnWidgetRemoteDao
import com.ataglance.walletglance.personalization.data.remote.dao.WidgetRemoteDao
import com.ataglance.walletglance.personalization.data.remote.source.BudgetOnWidgetRemoteDataSource
import com.ataglance.walletglance.personalization.data.remote.source.BudgetOnWidgetRemoteDataSourceImpl
import com.ataglance.walletglance.personalization.data.remote.source.WidgetRemoteDataSource
import com.ataglance.walletglance.personalization.data.remote.source.WidgetRemoteDataSourceImpl
import com.ataglance.walletglance.personalization.data.repository.BudgetOnWidgetRepository
import com.ataglance.walletglance.personalization.data.repository.BudgetOnWidgetRepositoryImpl
import com.ataglance.walletglance.personalization.data.repository.WidgetRepository
import com.ataglance.walletglance.personalization.data.repository.WidgetRepositoryImpl
import com.ataglance.walletglance.personalization.domain.usecase.GetBudgetIdsOnWidgetUseCase
import com.ataglance.walletglance.personalization.domain.usecase.GetBudgetIdsOnWidgetUseCaseImpl
import com.ataglance.walletglance.personalization.domain.usecase.GetWidgetsUseCase
import com.ataglance.walletglance.personalization.domain.usecase.GetWidgetsUseCaseImpl
import com.ataglance.walletglance.personalization.domain.usecase.SaveBudgetsOnWidgetUseCase
import com.ataglance.walletglance.personalization.domain.usecase.SaveBudgetsOnWidgetUseCaseImpl
import com.ataglance.walletglance.personalization.domain.usecase.SaveWidgetsUseCase
import com.ataglance.walletglance.personalization.domain.usecase.SaveWidgetsUseCaseImpl
import com.ataglance.walletglance.personalization.presentation.viewmodel.PersonalizationViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val personalizationModule = module {

    /* ---------- DAOs ---------- */

    single {
        WidgetRemoteDao(
            firestoreAdapter = FirestoreAdapterFactory(firestore = get()).getWidgetFirestoreAdapter()
        )
    }

    single {
        BudgetOnWidgetRemoteDao(
            firestoreAdapter = FirestoreAdapterFactory(firestore = get())
                .getBudgetOnWidgetFirestoreAdapter()
        )
    }

    /* ---------- Data Sources ---------- */

    single<WidgetLocalDataSource> {
        getWidgetLocalDataSource(appDatabase = get())
    }

    single<WidgetRemoteDataSource> {
        WidgetRemoteDataSourceImpl(widgetDao = get(), updateTimeDao = get())
    }

    single<BudgetOnWidgetLocalDataSource> {
        getBudgetOnWidgetLocalDataSource(appDatabase = get())
    }

    single<BudgetOnWidgetRemoteDataSource> {
        BudgetOnWidgetRemoteDataSourceImpl(budgetOnWidgetDao = get(), updateTimeDao = get())
    }

    /* ---------- Repositories ---------- */

    single<WidgetRepository> {
        WidgetRepositoryImpl(localSource = get(), remoteSource = get(), userContext = get())
    }

    single<BudgetOnWidgetRepository> {
        BudgetOnWidgetRepositoryImpl(localSource = get(), remoteSource = get(), userContext = get())
    }

    /* ---------- Use Cases ---------- */

    single<SaveWidgetsUseCase> {
        SaveWidgetsUseCaseImpl(widgetRepository = get())
    }

    single<GetWidgetsUseCase> {
        GetWidgetsUseCaseImpl(widgetRepository = get())
    }

    single<SaveBudgetsOnWidgetUseCase> {
        SaveBudgetsOnWidgetUseCaseImpl(budgetOnWidgetRepository = get())
    }

    single<GetBudgetIdsOnWidgetUseCase> {
        GetBudgetIdsOnWidgetUseCaseImpl(budgetOnWidgetRepository = get())
    }

    /* ---------- View Models ---------- */

    viewModel {
        PersonalizationViewModel(
            saveWidgetsUseCase = get(),
            getWidgetsUseCase = get(),
            saveBudgetsOnWidgetUseCase = get(),
            getBudgetIdsOnWidgetUseCase = get()
        )
    }

}