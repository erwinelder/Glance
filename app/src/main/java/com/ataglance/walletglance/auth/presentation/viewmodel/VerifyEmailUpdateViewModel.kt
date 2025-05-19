package com.ataglance.walletglance.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.R
import com.ataglance.walletglance.auth.domain.usecase.VerifyEmailUpdateUseCase
import com.ataglance.walletglance.errorHandling.mapper.toResultWithButtonState
import com.ataglance.walletglance.errorHandling.presentation.model.RequestState
import com.ataglance.walletglance.errorHandling.presentation.model.ResultWithButtonState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VerifyEmailUpdateViewModel(
    private val oobCode: String,
    private val verifyEmailUpdateUseCase: VerifyEmailUpdateUseCase
) : ViewModel() {

    fun verifyEmail() {
        setRequestLoadingState()

        viewModelScope.launch {
            val result = verifyEmailUpdateUseCase.execute(oobCode = oobCode)
            setRequestResultState(result = result.toResultWithButtonState())
        }
    }


    private val _requestState = MutableStateFlow<RequestState?>(null)
    val requestState = _requestState.asStateFlow()

    private fun setRequestLoadingState() {
        _requestState.update {
            RequestState.Loading(messageRes = R.string.verifying_your_email_loader)
        }
    }

    private fun setRequestResultState(result: ResultWithButtonState) {
        _requestState.update { RequestState.Result(resultState = result) }
    }

    fun resetRequestState() {
        _requestState.update { null }
    }


    init {
        verifyEmail()
    }

}