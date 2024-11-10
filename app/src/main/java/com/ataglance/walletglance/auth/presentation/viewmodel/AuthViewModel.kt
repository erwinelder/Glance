package com.ataglance.walletglance.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.ataglance.walletglance.core.domain.componentState.FieldWithValidationState
import com.ataglance.walletglance.core.utils.isValidEmail
import com.ataglance.walletglance.core.utils.isValidPassword
import com.ataglance.walletglance.core.utils.validateEmail
import com.ataglance.walletglance.core.utils.validatePassword
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AuthViewModel : ViewModel() {

    private val _emailState: MutableStateFlow<FieldWithValidationState> = MutableStateFlow(
        FieldWithValidationState()
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
        FieldWithValidationState()
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


    private val _signInIsAllowed: MutableStateFlow<Boolean> = MutableStateFlow(
        emailState.value.fieldText.isValidEmail() && passwordState.value.fieldText.isValidPassword()
    )
    val signInIsAllowed = _signInIsAllowed.asStateFlow()

    private val _signUpIsAllowed: MutableStateFlow<Boolean> = MutableStateFlow(
        emailState.value.isValid() && passwordState.value.isValid()
    )

    val signUpIsAllowed = _signUpIsAllowed.asStateFlow()

}