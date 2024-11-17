package com.ataglance.walletglance.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.core.domain.componentState.FieldWithValidationState
import com.ataglance.walletglance.core.utils.isValidEmail
import com.ataglance.walletglance.core.utils.isValidPassword
import com.ataglance.walletglance.core.utils.validateConfirmationPassword
import com.ataglance.walletglance.core.utils.validateEmail
import com.ataglance.walletglance.core.utils.validatePassword
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class AuthViewModel(
    email: String,
    password: String
) : ViewModel() {

    private val _emailState: MutableStateFlow<FieldWithValidationState> = MutableStateFlow(
        FieldWithValidationState(
            fieldText = email,
            validationStates = email.validateEmail()
        )
    )
    val emailState = _emailState.asStateFlow()

    fun updateEmail(email: String) {
        _emailState.update {
            it.copy(
                fieldText = email,
                validationStates = email.validateEmail()
            )
        }
    }


    private val _passwordState: MutableStateFlow<FieldWithValidationState> = MutableStateFlow(
        FieldWithValidationState(
            fieldText = password,
            validationStates = password.validatePassword()
        )
    )
    val passwordState = _passwordState.asStateFlow()

    fun updatePassword(password: String) {
        _passwordState.update {
            it.copy(
                fieldText = password,
                validationStates = password.validatePassword()
            )
        }
    }


    private val _confirmPasswordState: MutableStateFlow<FieldWithValidationState> =
        MutableStateFlow(FieldWithValidationState())
    val confirmPasswordState = _confirmPasswordState.asStateFlow()

    fun updateConfirmPassword(password: String) {
        _confirmPasswordState.update {
            it.copy(
                fieldText = password,
                validationStates = password
                    .validateConfirmationPassword(passwordState.value.fieldText)
            )
        }
    }


    private val _newPasswordState: MutableStateFlow<FieldWithValidationState> = MutableStateFlow(
        FieldWithValidationState(
            fieldText = password,
            validationStates = password.validatePassword()
        )
    )
    val newPasswordState = _newPasswordState.asStateFlow()

    fun updateNewPassword(password: String) {
        _newPasswordState.update {
            it.copy(
                fieldText = password,
                validationStates = password.validatePassword()
            )
        }
    }


    private val _newPasswordConfirmationState: MutableStateFlow<FieldWithValidationState> =
        MutableStateFlow(FieldWithValidationState())
    val newPasswordConfirmationState = _newPasswordConfirmationState.asStateFlow()

    fun updateNewPasswordConfirmation(password: String) {
        _newPasswordConfirmationState.update {
            it.copy(
                fieldText = password,
                validationStates = password
                    .validateConfirmationPassword(newPasswordState.value.fieldText)
            )
        }
    }


    val signInIsAllowed: StateFlow<Boolean> = combine(
        emailState, passwordState
    ) { emailState, passwordState ->
        emailState.fieldText.isValidEmail() && passwordState.fieldText.isValidPassword()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = false
    )

    val signUpIsAllowed: StateFlow<Boolean> = combine(
        emailState, passwordState, confirmPasswordState
    ) { emailState, passwordState, confirmPasswordState ->
        emailState.fieldText.isValidEmail() && passwordState.fieldText.isValidPassword() &&
                passwordState.fieldText == confirmPasswordState.fieldText
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = false
    )

    val passwordUpdateIsAllowed: StateFlow<Boolean> = combine(
        newPasswordState, newPasswordConfirmationState
    ) { newPasswordState, newPasswordConfirmationState ->
        newPasswordState.fieldText.isValidPassword() &&
                newPasswordState.fieldText == newPasswordConfirmationState.fieldText
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = false
    )

}

data class AuthViewModelFactory(
    private val email: String = "",
    private val password: String = ""
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthViewModel(email, password) as T
    }
}