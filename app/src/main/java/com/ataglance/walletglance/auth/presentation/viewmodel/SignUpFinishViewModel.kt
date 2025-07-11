package com.ataglance.walletglance.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.R
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthError
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthSuccess
import com.ataglance.walletglance.auth.domain.usecase.auth.FinishSignUpUseCase
import com.ataglance.walletglance.auth.mapper.toResultStateButton
import com.ataglance.walletglance.request.domain.model.result.Result
import com.ataglance.walletglance.request.presentation.model.RequestState
import com.ataglance.walletglance.request.presentation.model.ResultState.ButtonState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpFinishViewModel(
    private val oobCode: String,
    private val finishSignUpUseCase: FinishSignUpUseCase
) : ViewModel() {

    private var emailVerified = false

    fun isEmailVerified(): Boolean = emailVerified


    fun finishSignUp() {
        setRequestLoadingState()

        viewModelScope.launch {
            val result = finishSignUpUseCase.execute(oobCode = oobCode)

            if (result is Result.Success) emailVerified = true

            setRequestResultState(result = result)
        }
    }


    private val _requestState = MutableStateFlow<RequestState<ButtonState, ButtonState>>(
        RequestState.Success(
            state = AuthSuccess.SignUpVerificationCodeReceived.toResultStateButton()
        )
    )
    val requestState = _requestState.asStateFlow()

    private fun setRequestLoadingState() {
        _requestState.update {
            RequestState.Loading(messageRes = R.string.finishing_your_registration_loader)
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
                state = AuthSuccess.SignUpVerificationCodeReceived.toResultStateButton()
            )
        }
        emailVerified = false
    }


    init {
        finishSignUp()
    }

}