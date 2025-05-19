package com.ataglance.walletglance.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.R
import com.ataglance.walletglance.auth.domain.usecase.SignInWithEmailAndPasswordUseCase
import com.ataglance.walletglance.auth.domain.validation.UserDataValidator
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

class SignInViewModel(
    email: String,
    private val signInUseCase: SignInWithEmailAndPasswordUseCase
) : ViewModel() {

    private val _emailState = MutableStateFlow(
        ValidatedFieldUiState(
            fieldText = email,
            validationStates = UserDataValidator.validateEmail(email).toUiStates()
        )
    )
    val emailState = _emailState.asStateFlow()

    fun updateAndValidateEmail(email: String) {
        _emailState.update {
            it.copy(
                fieldText = email,
                validationStates = UserDataValidator.validateEmail(email).toUiStates()
            )
        }
    }


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


    val signInIsAllowed = combine(
        emailState, passwordState
    ) { emailState, passwordState ->
        UserDataValidator.isValidEmail(email = emailState.fieldText) &&
                passwordState.fieldText.isNotBlank()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = false
    )


    private var signInJob: Job? = null

    fun signIn() {
        if (!signInIsAllowed.value) return
        setRequestLoadingState()

        signInJob = viewModelScope.launch {
            val result = signInUseCase.execute(
                email = emailState.value.getTrimmedText(),
                password = passwordState.value.getTrimmedText()
            )
            setRequestResultState(result = result.toResultWithButtonState())
        }
    }

    fun cancelSignIn() {
        signInJob?.cancel()
        signInJob = null
        resetRequestState()
    }


    private val _requestState = MutableStateFlow<RequestState?>(null)
    val requestState = _requestState.asStateFlow()

    private fun setRequestLoadingState() {
        _requestState.update {
            RequestState.Loading(messageRes = R.string.verifying_your_credentials_loader)
        }
    }

    private fun setRequestResultState(result: ResultWithButtonState) {
        _requestState.update { RequestState.Result(resultState = result) }
    }

    fun resetRequestState() {
        _requestState.update { null }
    }

}