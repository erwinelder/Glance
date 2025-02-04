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
import com.ataglance.walletglance.record.domain.usecase.GetRecordsInDateRangeUseCase
import com.ataglance.walletglance.record.domain.usecase.GetRecordsInDateRangeUseCaseImpl
import com.ataglance.walletglance.record.domain.usecase.GetTodayTotalExpensesForAccount
import com.ataglance.walletglance.record.domain.usecase.GetTodayTotalExpensesForAccountImpl
import org.koin.dsl.module

val recordModule = module {

    /* ---------- DAOs ---------- */

    single {
        RecordRemoteDao(
            firestoreAdapter = FirestoreAdapterFactory(firestore = get()).getRecordFirestoreAdapter()
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
        RecordRepositoryImpl(localSource = get(), remoteSource = get(), userContext = get())
    }

    /* ---------- Use Cases ---------- */

    single<GetLastRecordNumUseCase> {
        GetLastRecordNumUseCaseImpl(recordRepository = get())
    }

    single<GetRecordsInDateRangeUseCase> {
        GetRecordsInDateRangeUseCaseImpl(
            recordRepository = get(),
            getAllAccountsUseCase = get(),
            getAllCategoriesUseCase = get()
        )
    }

    single<GetTodayTotalExpensesForAccount> {
        GetTodayTotalExpensesForAccountImpl(recordRepository = get())
    }

}