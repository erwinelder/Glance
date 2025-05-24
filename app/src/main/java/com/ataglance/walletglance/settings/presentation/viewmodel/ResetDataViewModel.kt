package com.ataglance.walletglance.settings.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.ataglance.walletglance.auth.domain.usecase.auth.SignOutUseCase
import com.ataglance.walletglance.core.domain.usecase.DeleteAllDataLocallyUseCase

class ResetDataViewModel(
    private val deleteAllDataLocallyUseCase: DeleteAllDataLocallyUseCase,
    private val signOutUseCase: SignOutUseCase,
) : ViewModel() {

    suspend fun resetData() {
        deleteAllDataLocallyUseCase.execute()
        signOutUseCase.execute()
    }

}