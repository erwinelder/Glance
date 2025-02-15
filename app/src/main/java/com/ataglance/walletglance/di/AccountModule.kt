package com.ataglance.walletglance.di

import com.ataglance.walletglance.account.data.local.source.AccountLocalDataSource
import com.ataglance.walletglance.account.data.local.source.getAccountLocalDataSource
import com.ataglance.walletglance.account.data.remote.dao.AccountRemoteDao
import com.ataglance.walletglance.account.data.remote.source.AccountRemoteDataSource
import com.ataglance.walletglance.account.data.remote.source.AccountRemoteDataSourceImpl
import com.ataglance.walletglance.account.data.repository.AccountRepository
import com.ataglance.walletglance.account.data.repository.AccountRepositoryImpl
import com.ataglance.walletglance.account.domain.usecase.GetAccountsUseCase
import com.ataglance.walletglance.account.domain.usecase.GetAccountsUseCaseImpl
import com.ataglance.walletglance.account.domain.usecase.SaveAccountsUseCase
import com.ataglance.walletglance.account.domain.usecase.SaveAccountsUseCaseImpl
import com.ataglance.walletglance.account.presentation.viewmodel.ActiveAccountCardViewModel
import com.ataglance.walletglance.account.presentation.viewmodel.EditAccountsViewModel
import com.ataglance.walletglance.core.data.remote.FirestoreAdapterFactory
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val accountModule = module {

    /* ---------- DAOs ---------- */

    single {
        AccountRemoteDao(
            firestoreAdapter = FirestoreAdapterFactory(firestore = get()).getAccountFirestoreAdapter()
        )
    }

    /* ---------- Data Sources ---------- */

    single<AccountLocalDataSource> {
        getAccountLocalDataSource(appDatabase = get())
    }

    single<AccountRemoteDataSource> {
        AccountRemoteDataSourceImpl(accountDao = get(), updateTimeDao = get())
    }

    /* ---------- Repositories ---------- */

    single<AccountRepository> {
        AccountRepositoryImpl(localSource = get(), remoteSource = get(), userContext = get())
    }

    /* ---------- Use Cases ---------- */

    single<SaveAccountsUseCase> {
        SaveAccountsUseCaseImpl(accountRepository = get(), recordRepository = get())
    }

    single<GetAccountsUseCase> {
        GetAccountsUseCaseImpl(accountRepository = get())
    }

    /* ---------- View Models ---------- */

    viewModel {
        EditAccountsViewModel(
            saveAccountsUseCase = get(),
            getAccountsUseCase = get()
        )
    }

    viewModel {
        ActiveAccountCardViewModel(getTodayTotalExpensesForAccountUseCase = get())
    }

}