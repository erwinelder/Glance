package com.ataglance.walletglance.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.auth.domain.model.validation.UserDataValidator
import com.ataglance.walletglance.auth.mapper.toUiStates
import com.ataglance.walletglance.request.presentation.model.ValidatedFieldState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class SignUpViewModel(
    email: String
) : ViewModel() {

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

}