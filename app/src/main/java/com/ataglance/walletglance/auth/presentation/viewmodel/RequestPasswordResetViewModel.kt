package com.ataglance.walletglance.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.R
import com.ataglance.walletglance.auth.domain.model.validation.UserDataValidator
import com.ataglance.walletglance.auth.domain.usecase.auth.RequestPasswordResetUseCase
import com.ataglance.walletglance.auth.mapper.toResultWithButtonState
import com.ataglance.walletglance.auth.mapper.toUiStates
import com.ataglance.walletglance.request.presentation.model.RequestState
import com.ataglance.walletglance.request.presentation.model.ResultState.ButtonState
import com.ataglance.walletglance.request.presentation.model.ValidatedFieldState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RequestPasswordResetViewModel(
    email: String,
    private val requestPasswordResetUseCase: RequestPasswordResetUseCase
) : ViewModel() {

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
        emailState.fieldText.isNotBlank()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = false
    )


    private var passwordResetRequestJob: Job? = null

    fun requestPasswordReset() {
        if (!requestIsAllowed.value) return
        setRequestLoadingState()

        passwordResetRequestJob = viewModelScope.launch {
            val result = requestPasswordResetUseCase.execute(
                email = emailState.value.trimmedText
            )
            setRequestResultState(result = result.toResultWithButtonState())
        }
    }

    fun cancelPasswordResetRequest() {
        passwordResetRequestJob?.cancel()
        passwordResetRequestJob = null
        resetRequestState()
    }


    private val _requestState = MutableStateFlow<RequestState<ButtonState>?>(null)
    val requestState = _requestState.asStateFlow()

    private fun setRequestLoadingState() {
        _requestState.update {
            RequestState.Loading(messageRes = R.string.requesting_password_reset_loader)
        }
    }

    private fun setRequestResultState(result: ButtonState) {
        _requestState.update { RequestState.Result(resultState = result) }
    }

    fun resetRequestState() {
        _requestState.update { null }
    }

}