package com.ataglance.walletglance.di

import com.ataglance.walletglance.budget.data.local.source.BudgetLocalDataSource
import com.ataglance.walletglance.budget.data.local.source.getBudgetLocalDataSource
import com.ataglance.walletglance.budget.data.remote.dao.BudgetRemoteDao
import com.ataglance.walletglance.budget.data.remote.source.BudgetRemoteDataSource
import com.ataglance.walletglance.budget.data.remote.source.BudgetRemoteDataSourceImpl
import com.ataglance.walletglance.budget.data.repository.BudgetRepository
import com.ataglance.walletglance.budget.data.repository.BudgetRepositoryImpl
import com.ataglance.walletglance.budget.domain.usecase.GetBudgetsUseCase
import com.ataglance.walletglance.budget.domain.usecase.GetBudgetsUseCaseImpl
import com.ataglance.walletglance.budget.domain.usecase.SaveBudgetsUseCase
import com.ataglance.walletglance.budget.domain.usecase.SaveBudgetsUseCaseImpl
import com.ataglance.walletglance.core.data.remote.FirestoreAdapterFactory
import org.koin.dsl.module

val budgetModule = module {

    /* ---------- DAOs ---------- */

    single {
        BudgetRemoteDao(
            budgetFirestoreAdapter = FirestoreAdapterFactory(firestore = get())
                .getBudgetFirestoreAdapter(),
            associationFirestoreAdapter = FirestoreAdapterFactory(firestore = get())
                .getBudgetAccountAssociationFirestoreAdapter()
        )
    }

    /* ---------- Data Sources ---------- */

    single<BudgetLocalDataSource> {
        getBudgetLocalDataSource(appDatabase = get())
    }

    single<BudgetRemoteDataSource> {
        BudgetRemoteDataSourceImpl(budgetDao = get(), updateTimeDao = get())
    }

    /* ---------- Repositories ---------- */

    single<BudgetRepository> {
        BudgetRepositoryImpl(localSource = get(), remoteSource = get(), userContext = get())
    }

    /* ---------- Use Cases ---------- */

    single<SaveBudgetsUseCase> {
        SaveBudgetsUseCaseImpl(budgetRepository = get())
    }

    single<GetBudgetsUseCase> {
        GetBudgetsUseCaseImpl(
            budgetRepository = get(),
            getExpenseCategoriesUseCase = get(),
            getAllAccountsUseCase = get(),
            getRecordsInDateRangeUseCase = get()
        )
    }

}