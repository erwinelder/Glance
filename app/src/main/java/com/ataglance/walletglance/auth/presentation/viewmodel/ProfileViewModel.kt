package com.ataglance.walletglance.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.ataglance.walletglance.auth.domain.usecase.auth.SignOutUseCase

class ProfileViewModel(
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    fun signOut() {
        signOutUseCase.execute()
    }

}