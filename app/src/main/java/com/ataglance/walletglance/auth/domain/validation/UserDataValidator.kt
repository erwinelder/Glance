package com.ataglance.walletglance.auth.domain.validation

import com.ataglance.walletglance.errorHandling.domain.model.validation.UserDataValidation
import com.ataglance.walletglance.errorHandling.domain.model.validation.ValidationResult

typealias UserDataValidationResult = ValidationResult<UserDataValidation>

class UserDataValidator {

    fun isValidEmail(email: String): Boolean {
        return validateEmail(email).all { it is ValidationResult.Success }
    }

    fun validateEmail(email: String): List<UserDataValidationResult> {
        return listOf(
            email.isValidEmail()
        )
    }


    fun isValidPassword(password: String): Boolean {
        return validatePassword(password).all { it is ValidationResult.Success }
    }

    fun validatePassword(password: String): List<UserDataValidationResult> {
        return listOf(
            password.atLeastEightChars(),
            password.atLeastOneUppercaseLetter(),
            password.atLeastOneLowercaseLetter(),
            password.atLeastOneDigit(),
            password.atLeastOneSpecChar()
        )
    }

    fun validateConfirmationPassword(
        password: String,
        confirmationPassword: String
    ): List<UserDataValidationResult> {
        return listOf(
            password.matchPassword(confirmationPassword)
        )
    }


    private fun String.isValidEmail(): UserDataValidationResult {
        return ValidationResult.fromValidation(
            validation = UserDataValidation.EmailValidity,
            isValid = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$").matches(this)
        )
    }

    private fun String.atLeastEightChars(): UserDataValidationResult {
        return ValidationResult.fromValidation(
            validation = UserDataValidation.AtLeastEightChars,
            isValid = Regex("^.{8,}$").matches(this)
        )
    }

    private fun String.atLeastOneUppercaseLetter(): UserDataValidationResult {
        return ValidationResult.fromValidation(
            validation = UserDataValidation.AtLeastOneUppercaseLetter,
            isValid = Regex(".*[A-Z].*").matches(this)
        )
    }

    private fun String.atLeastOneLowercaseLetter(): UserDataValidationResult {
        return ValidationResult.fromValidation(
            validation = UserDataValidation.AtLeastOneLowercaseLetter,
            isValid = Regex(".*[a-z].*").matches(this)
        )
    }

    private fun String.atLeastOneDigit(): UserDataValidationResult {
        return ValidationResult.fromValidation(
            validation = UserDataValidation.AtLeastOneDigit,
            isValid = Regex(".*\\d.*").matches(this)
        )
    }

    private fun String.atLeastOneSpecChar(): UserDataValidationResult {
        val specChars = "@\$!%*?&#_+-"
        return ValidationResult.fromValidation(
            validation = UserDataValidation.AtLeastOneSpecChar,
            isValid = Regex(".*[$specChars].*").matches(this)
        )
    }

    private fun String.matchPassword(password: String): UserDataValidationResult {
        return ValidationResult.fromValidation(
            validation = UserDataValidation.PasswordsMatching,
            isValid = this == password
        )
    }

}