package com.ataglance.walletglance.di

import com.ataglance.walletglance.account.data.local.source.AccountLocalDataSource
import com.ataglance.walletglance.account.data.local.source.getAccountLocalDataSource
import com.ataglance.walletglance.account.data.remote.source.AccountRemoteDataSource
import com.ataglance.walletglance.account.data.remote.source.AccountRemoteDataSourceImpl
import com.ataglance.walletglance.account.data.repository.AccountRepository
import com.ataglance.walletglance.account.data.repository.AccountRepositoryImpl
import com.ataglance.walletglance.account.domain.usecase.GetAccountsUseCase
import com.ataglance.walletglance.account.domain.usecase.GetAccountsUseCaseImpl
import com.ataglance.walletglance.account.domain.usecase.SaveAccountsUseCase
import com.ataglance.walletglance.account.domain.usecase.SaveAccountsUseCaseImpl
import com.ataglance.walletglance.account.presentation.viewmodel.ActiveAccountCardViewModel
import com.ataglance.walletglance.account.presentation.viewmodel.CurrencyPickerViewModel
import com.ataglance.walletglance.account.presentation.viewmodel.EditAccountsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val accountModule = module {

    /* ---------- Data Sources ---------- */

    single<AccountLocalDataSource> {
        getAccountLocalDataSource(appDatabase = get())
    }

    single<AccountRemoteDataSource> {
        AccountRemoteDataSourceImpl(
            client = get {
                parametersOf("account")
            }
        )
    }

    /* ---------- Repositories ---------- */

    single<AccountRepository> {
        AccountRepositoryImpl(
            localSource = get(),
            remoteSource = get(),
            syncHelper = get()
        )
    }

    /* ---------- Use Cases ---------- */

    single<SaveAccountsUseCase> {
        SaveAccountsUseCaseImpl(
            accountRepository = get(),
            transformAccountTransactionsToRecords = get()
        )
    }

    single<GetAccountsUseCase> {
        GetAccountsUseCaseImpl(accountRepository = get())
    }

    /* ---------- ViewModels ---------- */

    viewModel {
        EditAccountsViewModel(
            saveAccountsUseCase = get(),
            getAccountsUseCase = get()
        )
    }

    viewModel { params ->
        CurrencyPickerViewModel(selectedCurrency = params.getOrNull<String>())
    }

    viewModel {
        ActiveAccountCardViewModel(getTodayTotalExpensesForAccountUseCase = get())
    }

}