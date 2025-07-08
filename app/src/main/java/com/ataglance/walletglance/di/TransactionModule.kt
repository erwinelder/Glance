package com.ataglance.walletglance.di

import com.ataglance.walletglance.transaction.domain.usecase.GetTodayTotalExpensesForAccountUseCase
import com.ataglance.walletglance.transaction.domain.usecase.GetTodayTotalExpensesForAccountUseCaseImpl
import com.ataglance.walletglance.transaction.domain.usecase.GetTotalExpensesInDateRangesUseCase
import com.ataglance.walletglance.transaction.domain.usecase.GetTotalExpensesInDateRangesUseCaseImpl
import com.ataglance.walletglance.transaction.domain.usecase.GetTransactionsInDateRangeUseCase
import com.ataglance.walletglance.transaction.domain.usecase.GetTransactionsInDateRangeUseCaseImpl
import com.ataglance.walletglance.transaction.domain.usecase.TransformAccountTransactionsToRecords
import com.ataglance.walletglance.transaction.domain.usecase.TransformAccountTransactionsToRecordsImpl
import com.ataglance.walletglance.transaction.presentation.viewmodel.RecentTransactionsWidgetViewModel
import com.ataglance.walletglance.transaction.presentation.viewmodel.TransactionsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val transactionModule = module {

    /* ---------- Use Cases ---------- */

    single<TransformAccountTransactionsToRecords> {
        TransformAccountTransactionsToRecordsImpl(
            getTransfersByAccountsUseCase = get(),
            saveRecordsUseCase = get()
        )
    }

    single<GetTransactionsInDateRangeUseCase> {
        GetTransactionsInDateRangeUseCaseImpl(
            getRecordsInDateRangeUseCase = get(),
            getTransfersInDateRangeUseCase = get()
        )
    }

    single<GetTodayTotalExpensesForAccountUseCase> {
        GetTodayTotalExpensesForAccountUseCaseImpl(getTransactionsInDateRangeUseCase = get())
    }

    single<GetTotalExpensesInDateRangesUseCase> {
        GetTotalExpensesInDateRangesUseCaseImpl(
            getRecordsTotalExpensesInDateRange = get(),
            getTransfersTotalExpensesInDateRange = get()
        )
    }

    /* ---------- ViewModels ---------- */

    viewModel { parameters ->
        TransactionsViewModel(
            activeAccount = parameters.getOrNull(),
            activeDateRange = parameters.get(),
            resourceManager = get(),
            defaultCollectionName = parameters.get(),
            getAccountsUseCase = get(),
            getCategoriesUseCase = get(),
            getCategoryCollectionsUseCase = get(),
            getTransactionsInDateRangeUseCase = get()
        )
    }

    viewModel { parameters ->
        RecentTransactionsWidgetViewModel(
            activeAccount = parameters.getOrNull(),
            activeDateRange = parameters.get(),
            resourceManager = get(),
            getAccountsUseCase = get(),
            getCategoriesUseCase = get(),
            getTransactionsInDateRangeUseCase = get()
        )
    }

}