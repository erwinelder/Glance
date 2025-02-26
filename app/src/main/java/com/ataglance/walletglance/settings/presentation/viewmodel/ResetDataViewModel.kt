package com.ataglance.walletglance.settings.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.ataglance.walletglance.auth.domain.model.AuthController
import com.ataglance.walletglance.core.domain.usecase.DeleteAllDataLocallyUseCase

class ResetDataViewModel(
    private val deleteAllDataLocallyUseCase: DeleteAllDataLocallyUseCase,
    private val authController: AuthController
) : ViewModel() {

    suspend fun resetData() {
        deleteAllDataLocallyUseCase.execute()
        authController.signOut()
    }

}