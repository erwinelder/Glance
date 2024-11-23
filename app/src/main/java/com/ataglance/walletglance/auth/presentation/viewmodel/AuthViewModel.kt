package com.ataglance.walletglance.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.auth.domain.validation.UserDataValidator
import com.ataglance.walletglance.errorHandling.mapper.toUiStates
import com.ataglance.walletglance.errorHandling.presentation.model.ResultUiState
import com.ataglance.walletglance.errorHandling.presentation.model.ValidatedFieldUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class AuthViewModel(
    private val userDataValidator: UserDataValidator,
    email: String,
    password: String
) : ViewModel() {

    private val _emailState: MutableStateFlow<ValidatedFieldUiState> = MutableStateFlow(
        ValidatedFieldUiState(
            fieldText = email,
            validationStates = userDataValidator.validateEmail(email).toUiStates()
        )
    )
    val emailState = _emailState.asStateFlow()

    val emailIsValid: StateFlow<Boolean> = combine(_emailState) { emailStateArray ->
        userDataValidator.isValidEmail(emailStateArray[0].fieldText)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = false
    )

    fun updateEmail(email: String) {
        _emailState.update {
            it.copy(
                fieldText = email,
                validationStates = userDataValidator.validateEmail(email).toUiStates()
            )
        }
    }


    private val _passwordState: MutableStateFlow<ValidatedFieldUiState> = MutableStateFlow(
        ValidatedFieldUiState(
            fieldText = password,
            validationStates = userDataValidator.validatePassword(password).toUiStates()
        )
    )
    val passwordState = _passwordState.asStateFlow()

    fun updatePassword(password: String) {
        _passwordState.update {
            it.copy(
                fieldText = password,
                validationStates = userDataValidator.validatePassword(password)
                    .toUiStates()
            )
        }
    }


    private val _confirmPasswordState: MutableStateFlow<ValidatedFieldUiState> =
        MutableStateFlow(ValidatedFieldUiState())
    val confirmPasswordState = _confirmPasswordState.asStateFlow()

    fun updateConfirmPassword(password: String) {
        _confirmPasswordState.update {
            it.copy(
                fieldText = password,
                validationStates = userDataValidator
                    .validateConfirmationPassword(password, passwordState.value.fieldText)
                    .toUiStates()
            )
        }
    }


    private var obbCode = ""

    fun setObbCode(code: String) {
        obbCode = code
    }

    private val _newPasswordState: MutableStateFlow<ValidatedFieldUiState> = MutableStateFlow(
        ValidatedFieldUiState()
    )
    val newPasswordState = _newPasswordState.asStateFlow()

    fun updateNewPassword(password: String) {
        _newPasswordState.update {
            it.copy(
                fieldText = password,
                validationStates = userDataValidator.validatePassword(password)
                    .toUiStates()
            )
        }
    }


    private val _newPasswordConfirmationState: MutableStateFlow<ValidatedFieldUiState> =
        MutableStateFlow(ValidatedFieldUiState())
    val newPasswordConfirmationState = _newPasswordConfirmationState.asStateFlow()

    fun updateNewPasswordConfirmation(password: String) {
        _newPasswordConfirmationState.update {
            it.copy(
                fieldText = password,
                validationStates = userDataValidator
                    .validateConfirmationPassword(password, newPasswordState.value.fieldText)
                    .toUiStates()
            )
        }
    }


    val signInIsAllowed: StateFlow<Boolean> = combine(
        emailState, passwordState
    ) { emailState, passwordState ->
        userDataValidator.isValidEmail(emailState.fieldText) &&
                userDataValidator.isValidPassword(passwordState.fieldText)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = false
    )

    val signUpIsAllowed: StateFlow<Boolean> = combine(
        emailState, passwordState, confirmPasswordState
    ) { emailState, passwordState, confirmPasswordState ->
        userDataValidator.isValidEmail(emailState.fieldText) &&
                userDataValidator.isValidPassword(passwordState.fieldText) &&
                passwordState.fieldText == confirmPasswordState.fieldText
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = false
    )

    val passwordUpdateIsAllowed: StateFlow<Boolean> = combine(
        newPasswordState, newPasswordConfirmationState
    ) { newPasswordState, newPasswordConfirmationState ->
        userDataValidator.isValidPassword(newPasswordState.fieldText) &&
                newPasswordState.fieldText == newPasswordConfirmationState.fieldText
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = false
    )


    private val _resultState: MutableStateFlow<ResultUiState?> = MutableStateFlow(null)
    val resultState = _resultState.asStateFlow()

    fun setResultState(result: ResultUiState) {
        _resultState.update { result }
    }

    fun resetResultState() {
        _resultState.update { null }
    }


    fun resetAllFields() {
        updateEmail("")
        updatePassword("")
        updateConfirmPassword("")
        updateNewPassword("")
        updateNewPasswordConfirmation("")
        resetResultState()
    }

}

data class AuthViewModelFactory(
    private val email: String = "",
    private val password: String = ""
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthViewModel(
            userDataValidator = UserDataValidator(),
            email = email,
            password = password
        ) as T
    }
}