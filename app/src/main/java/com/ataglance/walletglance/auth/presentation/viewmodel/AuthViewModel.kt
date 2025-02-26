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
    email: String
) : ViewModel() {

    private var obbCode = ""

    fun setObbCode(code: String) {
        obbCode = code
    }


    private val _emailState: MutableStateFlow<ValidatedFieldUiState> = MutableStateFlow(
        ValidatedFieldUiState(
            fieldText = email,
            validationStates = userDataValidator.validateEmail(email).toUiStates()
        )
    )
    val emailState = _emailState.asStateFlow()

    val emailIsValid: StateFlow<Boolean> = combine(_emailState) { emailStateArray ->
        emailStateArray[0].isValid()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = false
    )

    fun updateAndValidateEmail(email: String) {
        _emailState.update {
            it.copy(
                fieldText = email,
                validationStates = userDataValidator.validateEmail(email).toUiStates()
            )
        }
    }


    private val _newEmailState: MutableStateFlow<ValidatedFieldUiState> = MutableStateFlow(
        ValidatedFieldUiState(
            validationStates = userDataValidator.validateEmail("").toUiStates()
        )
    )
    val newEmailState = _newEmailState.asStateFlow()

    fun updateAndValidateNewEmail(email: String) {
        _newEmailState.update {
            it.copy(
                fieldText = email,
                validationStates = userDataValidator.validateEmail(email).toUiStates()
            )
        }
    }

    private fun resetNewEmail() {
        updateAndValidateNewEmail("")
    }


    private val _passwordState: MutableStateFlow<ValidatedFieldUiState> = MutableStateFlow(
        ValidatedFieldUiState(
            fieldText = "",
            validationStates = userDataValidator.validateRequiredFieldIsNotEmpty("").toUiStates()
        )
    )
    val passwordState = _passwordState.asStateFlow()

    fun updateAndValidatePassword(password: String) {
        _passwordState.update {
            it.copy(
                fieldText = password,
                validationStates = userDataValidator.validateRequiredFieldIsNotEmpty(password)
                    .toUiStates()
            )
        }
    }

    private fun resetPassword() {
        updateAndValidatePassword("")
    }


    private val _newPasswordState: MutableStateFlow<ValidatedFieldUiState> = MutableStateFlow(
        ValidatedFieldUiState(
            validationStates = userDataValidator.validatePassword("").toUiStates()
        )
    )
    val newPasswordState = _newPasswordState.asStateFlow()

    fun updateAndValidateNewPassword(password: String) {
        _newPasswordState.update {
            it.copy(
                fieldText = password,
                validationStates = userDataValidator.validatePassword(password).toUiStates()
            )
        }
    }

    private fun resetNewPassword() {
        updateAndValidateNewPassword("")
    }


    private val _newPasswordConfirmationState: MutableStateFlow<ValidatedFieldUiState> =
        MutableStateFlow(
            ValidatedFieldUiState(
                validationStates = userDataValidator.validateConfirmationPassword("", "")
                    .toUiStates()
            )
        )
    val newPasswordConfirmationState = _newPasswordConfirmationState.asStateFlow()

    fun updateAndValidateNewPasswordConfirmation(password: String) {
        _newPasswordConfirmationState.update {
            it.copy(
                fieldText = password,
                validationStates = userDataValidator
                    .validateConfirmationPassword(newPasswordState.value.fieldText, password)
                    .toUiStates()
            )
        }
    }

    private fun resetNewPasswordConfirmation() {
        updateAndValidateNewPasswordConfirmation("")
    }


    val signInIsAllowed: StateFlow<Boolean> = combine(
        emailState, passwordState
    ) { emailState, passwordState ->
        userDataValidator.isValidEmail(emailState.fieldText) &&
                passwordState.fieldText.isNotEmpty()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = false
    )

    val signUpIsAllowed: StateFlow<Boolean> = combine(
        emailState, newPasswordState, newPasswordConfirmationState
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
        passwordState, newPasswordState, newPasswordConfirmationState
    ) { passwordState, newPasswordState, newPasswordConfirmationState ->
        passwordState.fieldText.isNotEmpty() &&
                userDataValidator.isValidPassword(newPasswordState.fieldText) &&
                newPasswordState.fieldText == newPasswordConfirmationState.fieldText
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = false
    )

    val passwordResetIsAllowed: StateFlow<Boolean> = combine(
        newPasswordState, newPasswordConfirmationState
    ) { newPasswordState, newPasswordConfirmationState ->
        userDataValidator.isValidPassword(newPasswordState.fieldText) &&
                newPasswordState.fieldText == newPasswordConfirmationState.fieldText
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = false
    )

    val emailUpdateIsAllowed: StateFlow<Boolean> = combine(
        passwordState, newEmailState
    ) { passwordState, newEmailState ->
        userDataValidator.isValidEmail(newEmailState.fieldText) &&
                passwordState.fieldText.isNotEmpty()
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


    fun resetAllFieldsExceptEmail() {
        resetNewEmail()
        resetPassword()
        resetNewPassword()
        resetNewPasswordConfirmation()
    }

}

data class AuthViewModelFactory(
    private val email: String = ""
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthViewModel(
            userDataValidator = UserDataValidator(),
            email = email
        ) as T
    }
}