package com.ataglance.walletglance.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.R
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthError
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthSuccess
import com.ataglance.walletglance.auth.domain.model.validation.UserDataValidator
import com.ataglance.walletglance.auth.domain.usecase.auth.RequestPasswordResetUseCase
import com.ataglance.walletglance.auth.mapper.toUiStates
import com.ataglance.walletglance.auth.mapperNew.toResultStateButton
import com.ataglance.walletglance.request.domain.model.result.Result
import com.ataglance.walletglance.request.presentation.model.ValidatedFieldState
import com.ataglance.walletglance.request.presentation.modelNew.RequestState
import com.ataglance.walletglance.request.presentation.modelNew.ResultState.ButtonState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PasswordResetRequestViewModel(
    email: String,
    private val requestPasswordResetUseCase: RequestPasswordResetUseCase
) : ViewModel() {

    /* ---------- Fields' states ---------- */

    private val _emailState = MutableStateFlow(
        ValidatedFieldState(
            fieldText = email,
            validationStates = UserDataValidator.validateRequiredFieldIsNotBlank(email).toUiStates()
        )
    )
    val emailState = _emailState.asStateFlow()

    fun updateAndValidateEmail(email: String) {
        _emailState.update {
            it.copy(
                fieldText = email,
                validationStates = UserDataValidator.validateRequiredFieldIsNotBlank(email)
                    .toUiStates()
            )
        }
    }


    val requestIsAllowed = emailState.map { emailState ->
        emailState.trimmedText.isNotBlank()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )


    /* ---------- Password reset request request state ---------- */

    private var passwordResetRequestJob: Job? = null

    fun requestPasswordReset() {
        if (!requestIsAllowed.value) return

        setRequestLoadingState()

        passwordResetRequestJob = viewModelScope.launch {
            val result = requestPasswordResetUseCase.execute(email = emailState.value.trimmedText)
            setRequestResultState(result = result)
        }
    }

    fun cancelPasswordResetRequest() {
        passwordResetRequestJob?.cancel()
        passwordResetRequestJob = null
        resetRequestState()
    }


    private val _requestState = MutableStateFlow<RequestState<ButtonState, ButtonState>?>(null)
    val requestState = _requestState.asStateFlow()

    private fun setRequestLoadingState() {
        _requestState.update {
            RequestState.Loading(messageRes = R.string.requesting_password_reset_loader)
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