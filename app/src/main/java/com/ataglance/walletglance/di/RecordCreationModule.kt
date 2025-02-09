package com.ataglance.walletglance.di

import com.ataglance.walletglance.recordCreation.domain.usecase.DeleteRecordUseCase
import com.ataglance.walletglance.recordCreation.domain.usecase.DeleteRecordUseCaseImpl
import com.ataglance.walletglance.recordCreation.domain.usecase.DeleteTransferUseCase
import com.ataglance.walletglance.recordCreation.domain.usecase.DeleteTransferUseCaseImpl
import com.ataglance.walletglance.recordCreation.domain.usecase.SaveRecordUseCase
import com.ataglance.walletglance.recordCreation.domain.usecase.SaveRecordUseCaseImpl
import com.ataglance.walletglance.recordCreation.domain.usecase.SaveTransferUseCase
import com.ataglance.walletglance.recordCreation.domain.usecase.SaveTransferUseCaseImpl
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

}