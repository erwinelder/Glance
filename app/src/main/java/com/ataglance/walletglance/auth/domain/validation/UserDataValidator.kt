package com.ataglance.walletglance.auth.domain.validation

import com.ataglance.walletglance.core.utils.asList
import com.ataglance.walletglance.errorHandling.domain.model.validation.UserDataValidation
import com.ataglance.walletglance.errorHandling.domain.model.validation.ValidationResult

typealias UserDataValidationResult = ValidationResult<UserDataValidation>

object UserDataValidator {

    fun isValidName(name: String): Boolean {
        return validateName(name).none { it is ValidationResult.Error }
    }

    fun validateName(name: String): List<UserDataValidationResult> {
        name.atLeastNumberOfChars(2).takeIfError()?.let { return listOf(it) }
        name.atMostNumberOfChars(30).takeIfError()?.let { return listOf(it) }

        return ValidationResult
            .fromValidation(validation = UserDataValidation.IsValid, isValid = true)
            .asList()
    }


    fun isValidEmail(email: String): Boolean {
        return validateEmail(email).all { it is ValidationResult.Success }
    }

    fun validateEmail(email: String): List<UserDataValidationResult> {
        email.requireNotBlank().takeIfError()?.let { return listOf(it) }
        return listOf(email.isValidEmail())
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
        confirmationPassword.requireNotBlank().takeIfError()?.let { return listOf(it) }
        return listOf(password.matchPassword(confirmationPassword))
    }


    fun validateRequiredFieldIsNotEmpty(value: String): List<UserDataValidationResult> {
        return listOf(value.requireNotBlank())
    }


    private fun String.requireNotBlank(): UserDataValidationResult {
        return ValidationResult.fromValidation(
            validation = UserDataValidation.RequiredField,
            isValid = this.isNotBlank()
        )
    }

    private fun String.isValidEmail(): UserDataValidationResult {
        val isValid = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$").matches(this)

        return ValidationResult.fromValidation(
            validation = if (isValid) UserDataValidation.IsValid else UserDataValidation.IsNotValid,
            isValid = isValid
        )
    }

    private fun String.atLeastNumberOfChars(number: Int): UserDataValidationResult {
        return ValidationResult.fromValidation(
            validation = UserDataValidation.TooShort,
            isValid = trim().length >= number
        )
    }

    private fun String.atMostNumberOfChars(number: Int): UserDataValidationResult {
        return ValidationResult.fromValidation(
            validation = UserDataValidation.TooLong,
            isValid = trim().length <= number
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