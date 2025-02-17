package com.ataglance.walletglance.di

import com.ataglance.walletglance.core.data.remote.FirestoreAdapterFactory
import com.ataglance.walletglance.record.data.local.source.RecordLocalDataSource
import com.ataglance.walletglance.record.data.local.source.getRecordLocalDataSource
import com.ataglance.walletglance.record.data.remote.dao.RecordRemoteDao
import com.ataglance.walletglance.record.data.remote.source.RecordRemoteDataSource
import com.ataglance.walletglance.record.data.remote.source.RecordRemoteDataSourceImpl
import com.ataglance.walletglance.record.data.repository.RecordRepository
import com.ataglance.walletglance.record.data.repository.RecordRepositoryImpl
import com.ataglance.walletglance.record.domain.usecase.GetLastRecordNumUseCase
import com.ataglance.walletglance.record.domain.usecase.GetLastRecordNumUseCaseImpl
import com.ataglance.walletglance.record.domain.usecase.GetRecordStackUseCase
import com.ataglance.walletglance.record.domain.usecase.GetRecordStackUseCaseImpl
import com.ataglance.walletglance.record.domain.usecase.GetRecordStacksInDateRangeUseCase
import com.ataglance.walletglance.record.domain.usecase.GetRecordStacksInDateRangeUseCaseImpl
import com.ataglance.walletglance.record.domain.usecase.GetRecordsInDateRangeUseCase
import com.ataglance.walletglance.record.domain.usecase.GetRecordsInDateRangeUseCaseImpl
import com.ataglance.walletglance.record.domain.usecase.GetRecordsTotalAmountInDateRangesUseCase
import com.ataglance.walletglance.record.domain.usecase.GetRecordsTotalAmountInDateRangesUseCaseImpl
import com.ataglance.walletglance.record.domain.usecase.GetTodayTotalExpensesForAccountUseCase
import com.ataglance.walletglance.record.domain.usecase.GetTodayTotalExpensesForAccountUseCaseImpl
import com.ataglance.walletglance.record.domain.usecase.GetTransferPairUseCase
import com.ataglance.walletglance.record.domain.usecase.GetTransferPairUseCaseImpl
import com.ataglance.walletglance.record.presentation.viewmodel.RecentRecordsWidgetViewModel
import com.ataglance.walletglance.record.presentation.viewmodel.RecordsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val recordModule = module {

    /* ---------- DAOs ---------- */

    single {
        RecordRemoteDao(
            firestoreAdapter = get<FirestoreAdapterFactory>().getRecordFirestoreAdapter()
        )
    }

    /* ---------- Data Sources ---------- */

    single<RecordLocalDataSource> {
        getRecordLocalDataSource(appDatabase = get())
    }

    single<RecordRemoteDataSource> {
        RecordRemoteDataSourceImpl(recordDao = get(), updateTimeDao = get())
    }

    /* ---------- Repositories ---------- */

    single<RecordRepository> {
        RecordRepositoryImpl(localSource = get(), remoteSource = get(), syncHelper = get())
    }

    /* ---------- Use Cases ---------- */

    single<GetLastRecordNumUseCase> {
        GetLastRecordNumUseCaseImpl(recordRepository = get())
    }

    single<GetRecordsInDateRangeUseCase> {
        GetRecordsInDateRangeUseCaseImpl(recordRepository = get())
    }

    single<GetRecordStackUseCase> {
        GetRecordStackUseCaseImpl(
            recordRepository = get(),
            getAccountsUseCase = get(),
            getCategoriesUseCase = get()
        )
    }

    single<GetTransferPairUseCase> {
        GetTransferPairUseCaseImpl(
            recordRepository = get(),
            getAccountsUseCase = get(),
            getCategoriesUseCase = get()
        )
    }

    single<GetRecordStacksInDateRangeUseCase> {
        GetRecordStacksInDateRangeUseCaseImpl(
            recordRepository = get(),
            getAccountsUseCase = get(),
            getCategoriesUseCase = get()
        )
    }

    single<GetTodayTotalExpensesForAccountUseCase> {
        GetTodayTotalExpensesForAccountUseCaseImpl(recordRepository = get())
    }

    single<GetRecordsTotalAmountInDateRangesUseCase> {
        GetRecordsTotalAmountInDateRangesUseCaseImpl(recordRepository = get())
    }

    /* ---------- View Models ---------- */

    viewModel { parameters ->
        RecordsViewModel(
            activeAccount = parameters.get(),
            activeDateRange = parameters.get(),
            defaultCollectionName = parameters.get(),
            getCategoryCollectionsUseCase = get(),
            getRecordStacksInDateRangeUseCase = get()
        )
    }

    viewModel { parameters ->
        RecentRecordsWidgetViewModel(
            activeAccount = parameters.get(),
            activeDateRange = parameters.get(),
            getRecordStacksInDateRangeUseCase = get()
        )
    }

}