package com.ataglance.walletglance.di

import com.ataglance.walletglance.budget.data.local.source.BudgetLocalDataSource
import com.ataglance.walletglance.budget.data.local.source.BudgetOnWidgetLocalDataSource
import com.ataglance.walletglance.budget.data.local.source.getBudgetLocalDataSource
import com.ataglance.walletglance.budget.data.local.source.getBudgetOnWidgetLocalDataSource
import com.ataglance.walletglance.budget.data.remote.dao.BudgetOnWidgetRemoteDao
import com.ataglance.walletglance.budget.data.remote.dao.BudgetRemoteDao
import com.ataglance.walletglance.budget.data.remote.source.BudgetOnWidgetRemoteDataSource
import com.ataglance.walletglance.budget.data.remote.source.BudgetOnWidgetRemoteDataSourceImpl
import com.ataglance.walletglance.budget.data.remote.source.BudgetRemoteDataSource
import com.ataglance.walletglance.budget.data.remote.source.BudgetRemoteDataSourceImpl
import com.ataglance.walletglance.budget.data.repository.BudgetOnWidgetRepository
import com.ataglance.walletglance.budget.data.repository.BudgetOnWidgetRepositoryImpl
import com.ataglance.walletglance.budget.data.repository.BudgetRepository
import com.ataglance.walletglance.budget.data.repository.BudgetRepositoryImpl
import com.ataglance.walletglance.budget.domain.usecase.GetBudgetIdsOnWidgetUseCase
import com.ataglance.walletglance.budget.domain.usecase.GetBudgetIdsOnWidgetUseCaseImpl
import com.ataglance.walletglance.budget.domain.usecase.GetBudgetsOnWidgetUseCase
import com.ataglance.walletglance.budget.domain.usecase.GetBudgetsOnWidgetUseCaseImpl
import com.ataglance.walletglance.budget.domain.usecase.GetBudgetsUseCase
import com.ataglance.walletglance.budget.domain.usecase.GetBudgetsUseCaseImpl
import com.ataglance.walletglance.budget.domain.usecase.SaveBudgetsOnWidgetUseCase
import com.ataglance.walletglance.budget.domain.usecase.SaveBudgetsOnWidgetUseCaseImpl
import com.ataglance.walletglance.budget.domain.usecase.SaveBudgetsUseCase
import com.ataglance.walletglance.budget.domain.usecase.SaveBudgetsUseCaseImpl
import com.ataglance.walletglance.budget.presentation.viewmodel.BudgetStatisticsViewModel
import com.ataglance.walletglance.budget.presentation.viewmodel.BudgetsOnWidgetSettingsViewModel
import com.ataglance.walletglance.budget.presentation.viewmodel.BudgetsOnWidgetViewModel
import com.ataglance.walletglance.budget.presentation.viewmodel.BudgetsViewModel
import com.ataglance.walletglance.budget.presentation.viewmodel.EditBudgetViewModel
import com.ataglance.walletglance.budget.presentation.viewmodel.EditBudgetsViewModel
import com.ataglance.walletglance.core.data.remote.FirestoreAdapterFactory
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val budgetModule = module {

    /* ---------- DAOs ---------- */

    single {
        BudgetRemoteDao(
            budgetFirestoreAdapter = get<FirestoreAdapterFactory>().getBudgetFirestoreAdapter(),
            associationFirestoreAdapter = get<FirestoreAdapterFactory>()
                .getBudgetAccountAssociationFirestoreAdapter()
        )
    }

    single {
        BudgetOnWidgetRemoteDao(
            firestoreAdapter = get<FirestoreAdapterFactory>().getBudgetOnWidgetFirestoreAdapter()
        )
    }

    /* ---------- Data Sources ---------- */

    single<BudgetLocalDataSource> {
        getBudgetLocalDataSource(appDatabase = get())
    }

    single<BudgetRemoteDataSource> {
        BudgetRemoteDataSourceImpl(budgetDao = get(), updateTimeDao = get())
    }

    single<BudgetOnWidgetLocalDataSource> {
        getBudgetOnWidgetLocalDataSource(appDatabase = get())
    }

    single<BudgetOnWidgetRemoteDataSource> {
        BudgetOnWidgetRemoteDataSourceImpl(budgetOnWidgetDao = get(), updateTimeDao = get())
    }

    /* ---------- Repositories ---------- */

    single<BudgetRepository> {
        BudgetRepositoryImpl(localSource = get(), remoteSource = get(), syncHelper = get())
    }

    single<BudgetOnWidgetRepository> {
        BudgetOnWidgetRepositoryImpl(localSource = get(), remoteSource = get(), userContext = get())
    }

    /* ---------- Use Cases ---------- */

    single<SaveBudgetsUseCase> {
        SaveBudgetsUseCaseImpl(budgetRepository = get())
    }

    single<GetBudgetsUseCase> {
        GetBudgetsUseCaseImpl(
            budgetRepository = get(),
            getCategoriesUseCase = get(),
            getAccountsUseCase = get(),
            getRecordsInDateRangeUseCase = get()
        )
    }

    single<SaveBudgetsOnWidgetUseCase> {
        SaveBudgetsOnWidgetUseCaseImpl(budgetOnWidgetRepository = get())
    }

    single<GetBudgetIdsOnWidgetUseCase> {
        GetBudgetIdsOnWidgetUseCaseImpl(budgetOnWidgetRepository = get())
    }

    single<GetBudgetsOnWidgetUseCase> {
        GetBudgetsOnWidgetUseCaseImpl(
            budgetRepository = get(),
            getBudgetIdsOnWidgetUseCase = get(),
            getAccountsUseCase = get(),
            getCategoriesUseCase = get(),
            getRecordsInDateRangeUseCase = get()
        )
    }

    /* ---------- View Models ---------- */

    viewModel {
        BudgetsViewModel(
            getBudgetsUseCase = get()
        )
    }

    viewModel { parameters ->
        BudgetStatisticsViewModel(
            budgetId = parameters.get(),
            getAccountsUseCase = get(),
            getBudgetsUseCase = get(),
            getRecordsTotalAmountInDateRangesUseCase = get(),
            resourceManager = get()
        )
    }

    viewModel {
        BudgetsOnWidgetViewModel(
            getBudgetsOnWidgetUseCase = get()
        )
    }

    viewModel {
        BudgetsOnWidgetSettingsViewModel(
            saveBudgetsOnWidgetUseCase = get(),
            getBudgetIdsOnWidgetUseCase = get(),
            getBudgetsUseCase = get()
        )
    }

    viewModel {
        EditBudgetsViewModel(
            saveBudgetsUseCase = get(),
            getBudgetsUseCase = get(),
            changeAppSetupStageUseCase = get()
        )
    }

    viewModel {
        EditBudgetViewModel(
            getAccountsUseCase = get(),
            getCategoriesUseCase = get()
        )
    }

}