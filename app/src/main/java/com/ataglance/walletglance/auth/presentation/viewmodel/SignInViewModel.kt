package com.ataglance.walletglance.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.R
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthError
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthSuccess
import com.ataglance.walletglance.auth.domain.model.validation.UserDataValidator
import com.ataglance.walletglance.auth.domain.usecase.auth.SignInWithEmailAndPasswordUseCase
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

class SignInViewModel(
    email: String,
    private val signInUseCase: SignInWithEmailAndPasswordUseCase
) : ViewModel() {

    /* ---------- Fields' states ---------- */

    private val _emailState = MutableStateFlow(
        ValidatedFieldState(
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


    val signInIsAllowed = combine(
        emailState, passwordState
    ) { emailState, passwordState ->
        UserDataValidator.isValidEmail(email = emailState.fieldText) &&
                passwordState.fieldText.isNotBlank()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )


    /* ---------- Sign in request state ---------- */

    private var signInJob: Job? = null

    fun signIn() {
        if (!signInIsAllowed.value) return
        setRequestLoadingState()

        signInJob = viewModelScope.launch {
            val result = signInUseCase.execute(
                email = emailState.value.trimmedText,
                password = passwordState.value.trimmedText
            )
            setRequestResultState(result = result)
        }
    }

    fun cancelSignIn() {
        signInJob?.cancel()
        signInJob = null
        resetRequestState()
    }


    private val _requestState = MutableStateFlow<RequestState<ButtonState, ButtonState>?>(null)
    val requestState = _requestState.asStateFlow()

    private fun setRequestLoadingState() {
        _requestState.update {
            RequestState.Loading(messageRes = R.string.verifying_your_credentials_loader)
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