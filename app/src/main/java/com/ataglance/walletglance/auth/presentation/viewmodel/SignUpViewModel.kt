package com.ataglance.walletglance.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.R
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthError
import com.ataglance.walletglance.auth.domain.model.errorHandling.AuthSuccess
import com.ataglance.walletglance.auth.domain.model.validation.UserDataValidator
import com.ataglance.walletglance.auth.domain.usecase.auth.CheckEmailVerificationUseCase
import com.ataglance.walletglance.auth.domain.usecase.auth.SignUpUseCase
import com.ataglance.walletglance.auth.mapper.toUiStates
import com.ataglance.walletglance.auth.mapper.toResultStateButton
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

class SignUpViewModel(
    email: String,
    private val signUpUseCase: SignUpUseCase,
    private val checkEmailVerificationUseCase: CheckEmailVerificationUseCase
) : ViewModel() {

    /* ---------- Fields' states ---------- */

    private val _nameState = MutableStateFlow(
        ValidatedFieldState(
            validationStates = UserDataValidator.validateName("").toUiStates()
        )
    )
    val nameState = _nameState.asStateFlow()

    fun updateAndValidateName(name: String) {
        _nameState.update {
            it.copy(
                fieldText = name,
                validationStates = UserDataValidator.validateName(name).toUiStates()
            )
        }
    }


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
            validationStates = UserDataValidator.validatePassword("").toUiStates()
        )
    )
    val passwordState = _passwordState.asStateFlow()

    fun updateAndValidatePassword(password: String) {
        _passwordState.update {
            it.copy(
                fieldText = password,
                validationStates = UserDataValidator.validatePassword(password).toUiStates()
            )
        }
    }


    private val _confirmPasswordState = MutableStateFlow(
        ValidatedFieldState(
            validationStates = UserDataValidator
                .validateConfirmationPassword(password = "", confirmationPassword = "")
                .toUiStates()
        )
    )
    val confirmPasswordState = _confirmPasswordState.asStateFlow()

    fun updateAndValidateConfirmPassword(password: String) {
        _confirmPasswordState.update {
            it.copy(
                fieldText = password,
                validationStates = UserDataValidator
                    .validateConfirmationPassword(
                        password = passwordState.value.fieldText, confirmationPassword = password
                    )
                    .toUiStates()
            )
        }
    }


    val signUpIsAllowed = combine(
        nameState, emailState, passwordState, confirmPasswordState
    ) { nameState, emailState, passwordState, confirmPasswordState ->
        UserDataValidator.isValidName(name = nameState.trimmedText) &&
                UserDataValidator.isValidEmail(email = emailState.trimmedText) &&
                UserDataValidator.isValidPassword(password = passwordState.trimmedText) &&
                passwordState.trimmedText == confirmPasswordState.trimmedText
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )


    /* ---------- Sign up request state ---------- */

    suspend fun signUp(): Boolean {
        if (!signUpIsAllowed.value) return false

        setSignUpRequestLoadingState()

        val result = signUpUseCase.execute(
            name = nameState.value.trimmedText,
            email = emailState.value.trimmedText,
            password = passwordState.value.trimmedText
        )

        return when (result) {
            is Result.Success -> {
                setEmailVerificationRequestResultState(result = result)
                resetSignUpRequestState()
                true
            }
            is Result.Error -> {
                setSignUpRequestResultState(result = result)
                false
            }
        }
    }


    private val _signUpRequestState = MutableStateFlow<RequestState<ButtonState, ButtonState>?>(null)
    val signUpRequestState = _signUpRequestState.asStateFlow()

    private fun setSignUpRequestLoadingState() {
        _signUpRequestState.update {
            RequestState.Loading(messageRes = R.string.creating_your_identity_loader)
        }
    }

    private fun setSignUpRequestResultState(result: Result<AuthSuccess, AuthError>) {
        _signUpRequestState.update {
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

    fun resetSignUpRequestState() {
        _signUpRequestState.update { null }
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
                email = emailState.value.trimmedText,
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