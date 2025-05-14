package com.ataglance.walletglance.errorHandling.domain.model.validation

enum class UserDataValidation: Validation {
    IsValid,
    IsNotValid,
    RequiredField,
    TooShort,
    TooLong,
    AtLeastEightChars,
    AtLeastOneUppercaseLetter,
    AtLeastOneLowercaseLetter,
    AtLeastOneDigit,
    AtLeastOneSpecChar,
    PasswordsMatching
}