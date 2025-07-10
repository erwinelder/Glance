package com.ataglance.walletglance.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.ataglance.walletglance.R
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthError
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthSuccess
import com.ataglance.walletglance.auth.domain.model.validation.UserDataValidator
import com.ataglance.walletglance.auth.domain.usecase.auth.SignUpUseCase
import com.ataglance.walletglance.auth.mapperNew.toResultStateButton
import com.ataglance.walletglance.request.domain.model.result.Result
import com.ataglance.walletglance.request.presentation.modelNew.RequestState
import com.ataglance.walletglance.request.presentation.modelNew.ResultState.ButtonState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SignUpRequestViewModel(
    private val signUpUseCase: SignUpUseCase,
) : ViewModel() {

    suspend fun signUp(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        val isValid = UserDataValidator.isValidName(name = name) &&
                UserDataValidator.isValidEmail(email = email) &&
                UserDataValidator.isValidPassword(password = password) &&
                password.trim() == confirmPassword.trim()
        if (!isValid) return false

        setRequestLoadingState()

        val result = signUpUseCase.execute(name = name, email = email, password = password)

        return when (result) {
            is Result.Success -> {
                resetRequestState()
                true
            }
            is Result.Error -> {
                setRequestResultState(result = result)
                false
            }
        }
    }


    private val _requestState = MutableStateFlow<RequestState<ButtonState, ButtonState>?>(null)
    val requestState = _requestState.asStateFlow()

    private fun setRequestLoadingState() {
        _requestState.update {
            RequestState.Loading(messageRes = R.string.creating_your_identity_loader)
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
        _requestState.update { null }
    }

}