package com.ataglance.walletglance.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.R
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthError
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthSuccess
import com.ataglance.walletglance.auth.domain.model.validation.UserDataValidator
import com.ataglance.walletglance.auth.domain.usecase.auth.CheckEmailVerificationUseCase
import com.ataglance.walletglance.auth.domain.usecase.auth.RequestEmailUpdateUseCase
import com.ataglance.walletglance.auth.mapper.toUiStates
import com.ataglance.walletglance.auth.mapper.toResultStateButton
import com.ataglance.walletglance.request.domain.model.result.Result
import com.ataglance.walletglance.request.presentation.model.ValidatedFieldState
import com.ataglance.walletglance.request.presentation.model.RequestState
import com.ataglance.walletglance.request.presentation.model.ResultState.ButtonState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EmailUpdateViewModel(
    private val requestEmailUpdateUseCase: RequestEmailUpdateUseCase,
    private val checkEmailVerificationUseCase: CheckEmailVerificationUseCase
) : ViewModel() {

    /* ---------- Fields' states ---------- */

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
                validationStates = UserDataValidator.validateRequiredFieldIsNotBlank(password)
                    .toUiStates()
            )
        }
    }


    private val _newEmailState = MutableStateFlow(
        ValidatedFieldState(
            validationStates = UserDataValidator.validateEmail("").toUiStates()
        )
    )
    val newEmailState = _newEmailState.asStateFlow()

    fun updateAndValidateNewEmail(email: String) {
        _newEmailState.update {
            it.copy(
                fieldText = email,
                validationStates = UserDataValidator.validateEmail(email).toUiStates()
            )
        }
    }


    val emailUpdateIsAllowed = combine(
        passwordState, newEmailState
    ) { passwordState, newEmailState ->
        passwordState.fieldText.isNotBlank() &&
                UserDataValidator.isValidEmail(email = newEmailState.fieldText)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )


    /* ---------- Update request request state ---------- */

    suspend fun requestEmailUpdate(): Boolean {
        if (!emailUpdateIsAllowed.value) return false

        setUpdateRequestRequestLoadingState()

        val result = requestEmailUpdateUseCase.execute(
            password = passwordState.value.trimmedText,
            newEmail = newEmailState.value.trimmedText
        )

        return when (result) {
            is Result.Success -> {
                setEmailVerificationRequestResultState(result = result)
                resetUpdateRequestRequestState()
                true
            }
            is Result.Error -> {
                setUpdateRequestRequestResultState(result = result)
                false
            }
        }
    }


    private val _updateRequestRequestState = MutableStateFlow<RequestState<ButtonState, ButtonState>?>(null)
    val updateRequestRequestState = _updateRequestRequestState.asStateFlow()

    private fun setUpdateRequestRequestLoadingState() {
        _updateRequestRequestState.update {
            RequestState.Loading(messageRes = R.string.requesting_email_update_loader)
        }
    }

    private fun setUpdateRequestRequestResultState(result: Result<AuthSuccess, AuthError>) {
        _updateRequestRequestState.update {
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

    fun resetUpdateRequestRequestState() {
        _updateRequestRequestState.update { null }
    }


    /* ---------- Check email verification request state ---------- */

    private var emailVerified = false

    fun isEmailVerified(): Boolean = emailVerified


    private var checkVerificationJob: Job? = null

    fun checkEmailVerification() {
        if (emailVerified) return

        setEmailVerificationRequestLoadingState()

        checkVerificationJob = viewModelScope.launch {
            val result = checkEmailVerificationUseCase.execute(
                email = newEmailState.value.trimmedText,
                password = passwordState.value.trimmedText
            )

            if (result is Result.Success) emailVerified = true

            setEmailVerificationRequestResultState(result = result)
        }
    }

    fun cancelEmailVerificationCheck() {
        checkVerificationJob?.cancel()
        checkVerificationJob = null
        resetEmailVerificationRequestState()
    }


    private val _emailVerificationRequestState = MutableStateFlow<RequestState<ButtonState, ButtonState>?>(null)
    val emailVerificationRequestState = _emailVerificationRequestState.asStateFlow()

    private fun setEmailVerificationRequestLoadingState() {
        _emailVerificationRequestState.update {
            RequestState.Loading(messageRes = R.string.checking_email_verification_loader)
        }
    }

    private fun setEmailVerificationRequestResultState(result: Result<AuthSuccess, AuthError>) {
        _emailVerificationRequestState.update {
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

    fun resetEmailVerificationRequestState() {
        _emailVerificationRequestState.update {
            RequestState.Success(
                state = AuthSuccess.SignUpEmailVerificationSent.toResultStateButton()
            )
        }
        emailVerified = false
    }

}