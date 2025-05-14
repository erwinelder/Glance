package com.ataglance.walletglance.errorHandling.mapper

import com.ataglance.walletglance.R
import com.ataglance.walletglance.errorHandling.domain.model.validation.UserDataValidation
import com.ataglance.walletglance.errorHandling.domain.model.validation.ValidationResult
import com.ataglance.walletglance.errorHandling.presentation.model.ValidationUiState


fun List<ValidationResult<UserDataValidation>>.toUiStates(): List<ValidationUiState> {
    return this.map { it.toResultState() }
}

fun ValidationResult<UserDataValidation>.toResultState(): ValidationUiState {
    val isValid = this is ValidationResult.Success

    return ValidationUiState(
        isValid = isValid,
        messageRes = when (this.validation) {
            UserDataValidation.IsValid -> R.string.is_valid
            UserDataValidation.IsNotValid -> R.string.is_not_valid
            UserDataValidation.RequiredField -> R.string.required_field
            UserDataValidation.TooShort -> R.string.too_short
            UserDataValidation.TooLong -> R.string.too_long
            UserDataValidation.AtLeastEightChars -> R.string.at_least_8_chars
            UserDataValidation.AtLeastOneUppercaseLetter -> R.string.at_least_1_uppercase_letter
            UserDataValidation.AtLeastOneLowercaseLetter -> R.string.at_least_1_lowercase_letter
            UserDataValidation.AtLeastOneDigit -> R.string.at_least_1_digit
            UserDataValidation.AtLeastOneSpecChar -> R.string.at_least_1_spec_char
            UserDataValidation.PasswordsMatching ->
                if (isValid) R.string.passwords_do_match else R.string.passwords_do_not_match
        }
    )
}