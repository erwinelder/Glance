package com.ataglance.walletglance.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.R
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthError
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthSuccess
import com.ataglance.walletglance.auth.domain.usecase.auth.CheckEmailVerificationUseCase
import com.ataglance.walletglance.auth.mapperNew.toResultStateButton
import com.ataglance.walletglance.request.domain.model.result.Result
import com.ataglance.walletglance.request.presentation.modelNew.RequestState
import com.ataglance.walletglance.request.presentation.modelNew.ResultState.ButtonState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpEmailVerificationViewModel(
    private val checkEmailVerificationUseCase: CheckEmailVerificationUseCase
) : ViewModel() {

    private val _emailVerified = MutableStateFlow(false)
    val emailVerified = _emailVerified.asStateFlow()


    private var checkVerificationJob: Job? = null

    fun checkEmailVerification(email: String, password: String) {
        setRequestLoadingState()

        checkVerificationJob = viewModelScope.launch {
            val result = checkEmailVerificationUseCase.execute(email = email, password = password)

            if (result is Result.Success) {
                _emailVerified.update { true }
            }

            setRequestResultState(result = result)
        }
    }

    fun cancelEmailVerificationCheck() {
        checkVerificationJob?.cancel()
        checkVerificationJob = null
        resetRequestState()
    }


    private val _requestState = MutableStateFlow<RequestState<ButtonState, ButtonState>>(
        RequestState.Success(state = AuthSuccess.SignUpEmailVerificationSent.toResultStateButton())
    )
    val requestState = _requestState.asStateFlow()

    private fun setRequestLoadingState() {
        _requestState.update {
            RequestState.Loading(messageRes = R.string.checking_email_verification_loader)
        }
    }

    private fun setRequestResultState(result: Result<AuthSuccess, AuthError>) {
        _requestState.update {
            when (result) {
                is Result.Success -> RequestState.Success(
                    state = result.success.toResultStateButton()
                )
                is Result.Error -> RequestState.Error(
                    state = result.error.toResultStateButton()
                )
            }
        }
    }

    fun resetRequestState() {
        _requestState.update {
            RequestState.Success(
                state = AuthSuccess.SignUpEmailVerificationSent.toResultStateButton()
            )
        }
        _emailVerified.update { false }
    }

}