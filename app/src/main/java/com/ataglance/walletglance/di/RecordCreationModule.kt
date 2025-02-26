package com.ataglance.walletglance.di

import com.ataglance.walletglance.recordCreation.domain.usecase.DeleteRecordUseCase
import com.ataglance.walletglance.recordCreation.domain.usecase.DeleteRecordUseCaseImpl
import com.ataglance.walletglance.recordCreation.domain.usecase.DeleteTransferUseCase
import com.ataglance.walletglance.recordCreation.domain.usecase.DeleteTransferUseCaseImpl
import com.ataglance.walletglance.recordCreation.domain.usecase.GetRecordDraftUseCase
import com.ataglance.walletglance.recordCreation.domain.usecase.GetRecordDraftUseCaseImpl
import com.ataglance.walletglance.recordCreation.domain.usecase.GetTransferDraftUseCase
import com.ataglance.walletglance.recordCreation.domain.usecase.GetTransferDraftUseCaseImpl
import com.ataglance.walletglance.recordCreation.domain.usecase.SaveRecordUseCase
import com.ataglance.walletglance.recordCreation.domain.usecase.SaveRecordUseCaseImpl
import com.ataglance.walletglance.recordCreation.domain.usecase.SaveTransferUseCase
import com.ataglance.walletglance.recordCreation.domain.usecase.SaveTransferUseCaseImpl
import com.ataglance.walletglance.recordCreation.presentation.viewmodel.RecordCreationViewModel
import com.ataglance.walletglance.recordCreation.presentation.viewmodel.TransferCreationViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val recordCreationModule = module {

    /* ---------- Use Cases ---------- */

    single<SaveRecordUseCase> {
        SaveRecordUseCaseImpl(
            recordRepository = get(),
            getRecordStackUseCase = get(),
            getAccountsUseCase = get(),
            saveAccountsUseCase = get()
        )
    }

    single<DeleteRecordUseCase> {
        DeleteRecordUseCaseImpl(
            recordRepository = get(),
            getRecordStackUseCase = get(),
            getAccountsUseCase = get(),
            saveAccountsUseCase = get()
        )
    }

    single<GetRecordDraftUseCase> {
        GetRecordDraftUseCaseImpl(
            getRecordStackUseCase = get(),
            getAccountsUseCase = get(),
            getLastRecordNumUseCase = get()
        )
    }

    single<SaveTransferUseCase> {
        SaveTransferUseCaseImpl(
            recordRepository = get(),
            getTransferPairUseCase = get(),
            getAccountsUseCase = get(),
            saveAccountsUseCase = get()
        )
    }

    single<DeleteTransferUseCase> {
        DeleteTransferUseCaseImpl(
            recordRepository = get(),
            getTransferPairUseCase = get(),
            getAccountsUseCase = get(),
            saveAccountsUseCase = get()
        )
    }

    single<GetTransferDraftUseCase> {
        GetTransferDraftUseCaseImpl(
            getRecordStackUseCase = get(),
            getAccountsUseCase = get(),
            getLastRecordNumUseCase = get()
        )
    }

    /* ---------- View Models ---------- */

    viewModel { parameters ->
        RecordCreationViewModel(
            recordNum = parameters.getOrNull(),
            getRecordDraftUseCase = get(),
            saveRecordUseCase = get(),
            deleteRecordUseCase = get(),
            getLastUsedRecordCategoryUseCase = get(),
            getLastRecordNumUseCase = get(),
            getCategoriesUseCase = get()
        )
    }

    viewModel { parameters ->
        TransferCreationViewModel(
            recordNum = parameters.getOrNull(),
            saveTransferUseCase = get(),
            deleteTransferUseCase = get(),
            getTransferDraftUseCase = get(),
            getAccountsUseCase = get(),
            getLastRecordNumUseCase = get()
        )
    }

}