package com.ataglance.walletglance.di

import com.ataglance.walletglance.budget.data.local.source.BudgetLocalDataSource
import com.ataglance.walletglance.budget.data.local.source.BudgetOnWidgetLocalDataSource
import com.ataglance.walletglance.budget.data.local.source.getBudgetLocalDataSource
import com.ataglance.walletglance.budget.data.local.source.getBudgetOnWidgetLocalDataSource
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
import com.ataglance.walletglance.budget.domain.usecase.GetEmptyBudgetsUseCase
import com.ataglance.walletglance.budget.domain.usecase.GetEmptyBudgetsUseCaseImpl
import com.ataglance.walletglance.budget.domain.usecase.GetFilledBudgetsByTypeUseCase
import com.ataglance.walletglance.budget.domain.usecase.GetFilledBudgetsByTypeUseCaseImpl
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
import org.koin.core.module.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val budgetModule = module {

    /* ---------- Data Sources ---------- */

    single<BudgetLocalDataSource> {
        getBudgetLocalDataSource(appDatabase = get())
    }

    single<BudgetRemoteDataSource> {
        BudgetRemoteDataSourceImpl()
    }

    single<BudgetOnWidgetLocalDataSource> {
        getBudgetOnWidgetLocalDataSource(appDatabase = get())
    }

    single<BudgetOnWidgetRemoteDataSource> {
        BudgetOnWidgetRemoteDataSourceImpl()
    }

    /* ---------- Repositories ---------- */

    single<BudgetRepository> {
        BudgetRepositoryImpl(localSource = get(), remoteSource = get(), syncHelper = get())
    }

    single<BudgetOnWidgetRepository> {
        BudgetOnWidgetRepositoryImpl(localSource = get(), remoteSource = get(), syncHelper = get())
    }

    /* ---------- Use Cases ---------- */

    single<SaveBudgetsUseCase> {
        SaveBudgetsUseCaseImpl(budgetRepository = get())
    }

    single<GetFilledBudgetsByTypeUseCase> {
        GetFilledBudgetsByTypeUseCaseImpl(
            getEmptyBudgetsUseCase = get(),
            getTransactionsInDateRangeUseCase = get()
        )
    }

    single<GetEmptyBudgetsUseCase> {
        GetEmptyBudgetsUseCaseImpl(
            budgetRepository = get(),
            getCategoriesUseCase = get(),
            getAccountsUseCase = get()
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
            getEmptyBudgetsUseCase = get(),
            getBudgetIdsOnWidgetUseCase = get(),
            getTransactionsInDateRangeUseCase = get()
        )
    }

    /* ---------- ViewModels ---------- */

    viewModel {
        BudgetsViewModel(
            getFilledBudgetsByTypeUseCase = get()
        )
    }

    viewModel { parameters ->
        BudgetStatisticsViewModel(
            budgetId = parameters.get(),
            getAccountsUseCase = get(),
            getEmptyBudgetsUseCase = get(),
            getTotalExpensesInDateRangesUseCase = get(),
            resourceManager = get { parametersOf(parameters.get<String>()) }
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
            getFilledBudgetsByTypeUseCase = get()
        )
    }

    viewModel {
        EditBudgetsViewModel(
            saveBudgetsUseCase = get(),
            getFilledBudgetsByTypeUseCase = get(),
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