package com.ataglance.walletglance.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.R
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthError
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthSuccess
import com.ataglance.walletglance.auth.domain.model.validation.UserDataValidator
import com.ataglance.walletglance.auth.domain.usecase.auth.ResetPasswordUseCase
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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PasswordResetViewModel(
    private val oobCode: String,
    private val resetPasswordUseCase: ResetPasswordUseCase
) : ViewModel() {

    /* ---------- Fields' states ---------- */

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


    val passwordResetIsAllowed = combine(
        newPasswordState, confirmNewPasswordState
    ) { newPasswordState, confirmNewPasswordState ->
        UserDataValidator.isValidPassword(password = newPasswordState.fieldText) &&
                newPasswordState.trimmedText == confirmNewPasswordState.trimmedText
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )


    /* ---------- Password reset request state ---------- */

    private var passwordResetJob: Job? = null

    fun resetPassword() {
        if (!passwordResetIsAllowed.value) return

        setRequestLoadingState()

        passwordResetJob = viewModelScope.launch {
            val result = resetPasswordUseCase.execute(
                oobCode = oobCode,
                newPassword = newPasswordState.value.trimmedText
            )
            setRequestResultState(result = result)
        }
    }

    fun cancelPasswordReset() {
        passwordResetJob?.cancel()
        passwordResetJob = null
        resetRequestState()
    }


    private val _requestState = MutableStateFlow<RequestState<ButtonState, ButtonState>?>(null)
    val requestState = _requestState.asStateFlow()

    private fun setRequestLoadingState() {
        _requestState.update {
            RequestState.Loading(messageRes = R.string.updating_your_password_loader)
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