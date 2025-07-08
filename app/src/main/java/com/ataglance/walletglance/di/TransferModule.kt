package com.ataglance.walletglance.di

import com.ataglance.walletglance.transfer.data.local.source.TransferLocalDataSource
import com.ataglance.walletglance.transfer.data.local.source.getTransferLocalDataSource
import com.ataglance.walletglance.transfer.data.remote.source.TransferRemoteDataSource
import com.ataglance.walletglance.transfer.data.remote.source.TransferRemoteDataSourceImpl
import com.ataglance.walletglance.transfer.data.repository.TransferRepository
import com.ataglance.walletglance.transfer.data.repository.TransferRepositoryImpl
import com.ataglance.walletglance.transfer.domain.usecase.DeleteTransferUseCase
import com.ataglance.walletglance.transfer.domain.usecase.DeleteTransferUseCaseImpl
import com.ataglance.walletglance.transfer.domain.usecase.GetTransferDraftUseCase
import com.ataglance.walletglance.transfer.domain.usecase.GetTransferDraftUseCaseImpl
import com.ataglance.walletglance.transfer.domain.usecase.GetTransferUseCase
import com.ataglance.walletglance.transfer.domain.usecase.GetTransferUseCaseImpl
import com.ataglance.walletglance.transfer.domain.usecase.GetTransfersByAccountsUseCase
import com.ataglance.walletglance.transfer.domain.usecase.GetTransfersByAccountsUseCaseImpl
import com.ataglance.walletglance.transfer.domain.usecase.GetTransfersInDateRangeUseCase
import com.ataglance.walletglance.transfer.domain.usecase.GetTransfersInDateRangeUseCaseImpl
import com.ataglance.walletglance.transfer.domain.usecase.GetTransfersTotalExpensesInDateRange
import com.ataglance.walletglance.transfer.domain.usecase.GetTransfersTotalExpensesInDateRangeImpl
import com.ataglance.walletglance.transfer.domain.usecase.SaveTransferUseCase
import com.ataglance.walletglance.transfer.domain.usecase.SaveTransferUseCaseImpl
import com.ataglance.walletglance.transfer.presentation.viewmodel.TransferCreationViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val transferModule = module {

    /* ---------- Data Sources ---------- */

    single<TransferLocalDataSource> {
        getTransferLocalDataSource(appDatabase = get())
    }

    single<TransferRemoteDataSource> {
        TransferRemoteDataSourceImpl()
    }

    /* ---------- Repositories ---------- */

    single<TransferRepository> {
        TransferRepositoryImpl(
            localSource = get(),
            remoteSource = get(),
            syncHelper = get()
        )
    }

    /* ---------- Use Cases ---------- */

    single<DeleteTransferUseCase> {
        DeleteTransferUseCaseImpl(
            transferRepository = get(),
            getAccountsUseCase = get(),
            saveAccountsUseCase = get()
        )
    }

    single<SaveTransferUseCase> {
        SaveTransferUseCaseImpl(
            transferRepository = get(),
            getAccountsUseCase = get(),
            saveAccountsUseCase = get()
        )
    }

    single<GetTransferUseCase> {
        GetTransferUseCaseImpl(transferRepository = get())
    }

    single<GetTransferDraftUseCase> {
        GetTransferDraftUseCaseImpl(
            getTransferUseCase = get(),
            getAccountsUseCase = get()
        )
    }

    single<GetTransfersInDateRangeUseCase> {
        GetTransfersInDateRangeUseCaseImpl(transferRepository = get())
    }

    single<GetTransfersByAccountsUseCase> {
        GetTransfersByAccountsUseCaseImpl(transferRepository = get())
    }

    single<GetTransfersTotalExpensesInDateRange> {
        GetTransfersTotalExpensesInDateRangeImpl(transferRepository = get())
    }

    /* ---------- ViewModels ---------- */

    viewModel { params ->
        TransferCreationViewModel(
            transferId = params.getOrNull(),
            accountId = params.getOrNull(),
            saveTransferUseCase = get(),
            deleteTransferUseCase = get(),
            getTransferDraftUseCase = get(),
            getAccountsUseCase = get()
        )
    }

}