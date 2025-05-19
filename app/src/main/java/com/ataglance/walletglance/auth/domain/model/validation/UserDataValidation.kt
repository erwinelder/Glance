package com.ataglance.walletglance.auth.domain.model.validation

import com.ataglance.walletglance.errorHandling.domain.model.validation.Validation

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