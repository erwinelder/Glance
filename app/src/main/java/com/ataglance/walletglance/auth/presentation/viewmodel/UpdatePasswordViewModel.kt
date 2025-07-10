package com.ataglance.walletglance.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.R
import com.ataglance.walletglance.auth.domain.model.validation.UserDataValidator
import com.ataglance.walletglance.auth.domain.usecase.auth.UpdatePasswordUseCase
import com.ataglance.walletglance.auth.mapper.toResultWithButtonState
import com.ataglance.walletglance.auth.mapper.toUiStates
import com.ataglance.walletglance.request.presentation.model.RequestState
import com.ataglance.walletglance.request.presentation.model.ResultState.ButtonState
import com.ataglance.walletglance.request.presentation.model.ValidatedFieldState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UpdatePasswordViewModel(
    private val updatePasswordUseCase: UpdatePasswordUseCase
) : ViewModel() {

    private val _passwordState = MutableStateFlow(
        ValidatedFieldState(
            validationStates = UserDataValidator.validateRequiredFieldIsNotBlank("").toUiStates()
        )
    )
    val passwordState = _passwordState.asStateFlow()

    fun updateAndValidatePassword(password: String) {
        _passwordState.update {
            it.copy(
                fieldText = password,
                validationStates = UserDataValidator.validateRequiredFieldIsNotBlank(password).toUiStates()
            )
        }
    }


    private val _newPasswordState = MutableStateFlow(
        ValidatedFieldState(
            validationStates = UserDataValidator.validatePassword("").toUiStates()
        )
    )
    val newPasswordState = _newPasswordState.asStateFlow()

    fun updateAndValidateNewPassword(password: String) {
        _newPasswordState.update {
            it.copy(
                fieldText = password,
                validationStates = UserDataValidator.validatePassword(password).toUiStates()
            )
        }
    }


    private val _confirmNewPasswordState = MutableStateFlow(
        ValidatedFieldState(
            validationStates = UserDataValidator
                .validateConfirmationPassword(password = "", confirmationPassword = "")
                .toUiStates()
        )
    )
    val confirmNewPasswordState = _confirmNewPasswordState.asStateFlow()

    fun updateAndValidateConfirmNewPassword(password: String) {
        _confirmNewPasswordState.update {
            it.copy(
                fieldText = password,
                validationStates = UserDataValidator
                    .validateConfirmationPassword(
                        password = newPasswordState.value.fieldText, confirmationPassword = password
                    )
                    .toUiStates()
            )
        }
    }


    val passwordUpdateIsAllowed = combine(
        passwordState, newPasswordState, confirmNewPasswordState
    ) { passwordState, newPasswordState, confirmNewPasswordState ->
        passwordState.fieldText.isNotBlank() &&
                UserDataValidator.isValidPassword(password = newPasswordState.fieldText) &&
                newPasswordState.fieldText.trim() == confirmNewPasswordState.fieldText.trim()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = false
    )


    private var passwordUpdateJob: Job? = null

    fun updatePassword() {
        if (!passwordUpdateIsAllowed.value) return
        setRequestLoadingState()

        passwordUpdateJob = viewModelScope.launch {
            val result = updatePasswordUseCase.execute(
                password = passwordState.value.trimmedText,
                newPassword = newPasswordState.value.trimmedText
            )
            setRequestResultState(result = result.toResultWithButtonState())
        }
    }

    fun cancelPasswordUpdate() {
        passwordUpdateJob?.cancel()
        passwordUpdateJob = null
        resetRequestState()
    }


    private val _requestState = MutableStateFlow<RequestState<ButtonState>?>(null)
    val requestState = _requestState.asStateFlow()

    private fun setRequestLoadingState() {
        _requestState.update {
            RequestState.Loading(messageRes = R.string.updating_your_password_loader)
        }
    }

    private fun setRequestResultState(result: ButtonState) {
        _requestState.update { RequestState.Result(resultState = result) }
    }

    fun resetRequestState() {
        _requestState.update { null }
    }

}