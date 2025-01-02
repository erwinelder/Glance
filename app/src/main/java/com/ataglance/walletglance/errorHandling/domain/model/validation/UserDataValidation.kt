package com.ataglance.walletglance.errorHandling.domain.model.validation

enum class UserDataValidation: Validation {
    RequiredField,
    EmailValidity,
    AtLeastEightChars,
    AtLeastOneUppercaseLetter,
    AtLeastOneLowercaseLetter,
    AtLeastOneDigit,
    AtLeastOneSpecChar,
    PasswordsMatching
}