package com.ataglance.walletglance.di

import com.ataglance.walletglance.record.data.local.source.RecordLocalDataSource
import com.ataglance.walletglance.record.data.local.source.getRecordLocalDataSource
import com.ataglance.walletglance.record.data.remote.source.RecordRemoteDataSource
import com.ataglance.walletglance.record.data.remote.source.RecordRemoteDataSourceImpl
import com.ataglance.walletglance.record.data.repository.RecordRepository
import com.ataglance.walletglance.record.data.repository.RecordRepositoryImpl
import com.ataglance.walletglance.record.domain.usecase.DeleteRecordUseCase
import com.ataglance.walletglance.record.domain.usecase.DeleteRecordUseCaseImpl
import com.ataglance.walletglance.record.domain.usecase.GetLastUsedRecordCategoryUseCase
import com.ataglance.walletglance.record.domain.usecase.GetLastUsedRecordCategoryUseCaseImpl
import com.ataglance.walletglance.record.domain.usecase.GetRecordDraftUseCase
import com.ataglance.walletglance.record.domain.usecase.GetRecordDraftUseCaseImpl
import com.ataglance.walletglance.record.domain.usecase.GetRecordUseCase
import com.ataglance.walletglance.record.domain.usecase.GetRecordUseCaseImpl
import com.ataglance.walletglance.record.domain.usecase.GetRecordsInDateRangeUseCase
import com.ataglance.walletglance.record.domain.usecase.GetRecordsInDateRangeUseCaseImpl
import com.ataglance.walletglance.record.domain.usecase.GetRecordsTotalExpensesInDateRange
import com.ataglance.walletglance.record.domain.usecase.GetRecordsTotalExpensesInDateRangeImpl
import com.ataglance.walletglance.record.domain.usecase.SaveRecordUseCase
import com.ataglance.walletglance.record.domain.usecase.SaveRecordUseCaseImpl
import com.ataglance.walletglance.record.domain.usecase.SaveRecordsUseCase
import com.ataglance.walletglance.record.domain.usecase.SaveRecordsUseCaseImpl
import com.ataglance.walletglance.record.presentation.viewmodel.RecordCreationViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val recordModule = module {

    /* ---------- Data Sources ---------- */

    single<RecordLocalDataSource> {
        getRecordLocalDataSource(appDatabase = get())
    }

    single<RecordRemoteDataSource> {
        RecordRemoteDataSourceImpl()
    }

    /* ---------- Repositories ---------- */

    single<RecordRepository> {
        RecordRepositoryImpl(
            localSource = get(),
            remoteSource = get(),
            syncHelper = get()
        )
    }

    /* ---------- Use Cases ---------- */

    single<DeleteRecordUseCase> {
        DeleteRecordUseCaseImpl(
            recordRepository = get(),
            getAccountsUseCase = get(),
            saveAccountsUseCase = get()
        )
    }

    single<SaveRecordUseCase> {
        SaveRecordUseCaseImpl(
            recordRepository = get(),
            getRecordUseCase = get(),
            getAccountsUseCase = get(),
            saveAccountsUseCase = get()
        )
    }

    single<SaveRecordsUseCase> {
        SaveRecordsUseCaseImpl(recordRepository = get())
    }

    single<GetRecordUseCase> {
        GetRecordUseCaseImpl(recordRepository = get())
    }

    single<GetRecordDraftUseCase> {
        GetRecordDraftUseCaseImpl(
            getRecordUseCase = get(),
            getAccountsUseCase = get(),
            getCategoriesUseCase = get()
        )
    }

    single<GetRecordsInDateRangeUseCase> {
        GetRecordsInDateRangeUseCaseImpl(recordRepository = get())
    }

    single<GetRecordsTotalExpensesInDateRange> {
        GetRecordsTotalExpensesInDateRangeImpl(recordRepository = get())
    }

    single<GetLastUsedRecordCategoryUseCase> {
        GetLastUsedRecordCategoryUseCaseImpl(
            getCategoriesUseCase = get(),
            recordRepository = get()
        )
    }

    /* ---------- ViewModels ---------- */

    viewModel { params ->
        RecordCreationViewModel(
            recordId = params.getOrNull(),
            accountId = params.getOrNull(),
            deleteRecordUseCase = get(),
            saveRecordUseCase = get(),
            getRecordDraftUseCase = get(),
            getLastUsedRecordCategoryUseCase = get(),
            getAccountsUseCase = get(),
            getCategoriesUseCase = get()
        )
    }

}