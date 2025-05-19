package com.ataglance.walletglance.auth.presentation.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.R
import com.ataglance.walletglance.auth.domain.usecase.CheckEmailVerificationUseCase
import com.ataglance.walletglance.auth.domain.usecase.RequestEmailUpdateUseCase
import com.ataglance.walletglance.auth.domain.validation.UserDataValidator
import com.ataglance.walletglance.errorHandling.domain.model.result.Result
import com.ataglance.walletglance.errorHandling.mapper.toResultWithButtonState
import com.ataglance.walletglance.errorHandling.mapper.toUiStates
import com.ataglance.walletglance.errorHandling.presentation.model.RequestState
import com.ataglance.walletglance.errorHandling.presentation.model.ResultWithButtonState
import com.ataglance.walletglance.errorHandling.presentation.model.ValidatedFieldUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UpdateEmailViewModel(
    private val requestEmailUpdateUseCase: RequestEmailUpdateUseCase,
    private val checkEmailVerificationUseCase: CheckEmailVerificationUseCase
) : ViewModel() {

    private val _passwordState = MutableStateFlow(
        ValidatedFieldUiState(
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
        ValidatedFieldUiState(
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
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = false
    )


    suspend fun requestEmailUpdate(): Boolean {
        if (!emailUpdateIsAllowed.value) return false
        setRequestLoadingState(messageRes = R.string.requesting_email_update_loader)

        val result = requestEmailUpdateUseCase.execute(
            password = passwordState.value.getTrimmedText(),
            newEmail = newEmailState.value.getTrimmedText()
        )

        return when (result) {
            is Result.Success -> {
                resetRequestState()
                true
            }
            is Result.Error -> {
                setRequestResultState(result = result.error.toResultWithButtonState())
                false
            }
        }
    }


    private var checkVerificationJob: Job? = null

    fun checkEmailVerification() {
        setRequestLoadingState(messageRes = R.string.checking_email_verification_loader)

        checkVerificationJob = viewModelScope.launch {
            val result = checkEmailVerificationUseCase.execute(
                email = newEmailState.value.getTrimmedText(),
                password = passwordState.value.getTrimmedText()
            )
            setRequestResultState(result = result.toResultWithButtonState())
        }
    }

    fun cancelEmailVerificationCheck() {
        checkVerificationJob?.cancel()
        checkVerificationJob = null
        resetRequestState()
    }


    private val _requestState = MutableStateFlow<RequestState?>(null)
    val requestState = _requestState.asStateFlow()

    private fun setRequestLoadingState(@StringRes messageRes: Int) {
        _requestState.update {
            RequestState.Loading(messageRes = messageRes)
        }
    }

    private fun setRequestResultState(result: ResultWithButtonState) {
        _requestState.update { RequestState.Result(resultState = result) }
    }

    fun resetRequestState() {
        _requestState.update { null }
    }

}