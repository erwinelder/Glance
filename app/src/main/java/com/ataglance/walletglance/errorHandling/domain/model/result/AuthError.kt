package com.ataglance.walletglance.errorHandling.domain.model.result

enum class AuthError : Error {
    UserAlreadyExists,
    UserNotCreated,
    UserDataNotSaved,
    UserNotFound,
    UserNotSignedIn,
    InvalidEmail,
    InvalidCode,
    WrongCredentials,
    SignInError,
    SignUpEmailVerificationError,
    EmailForPasswordResetError,
    PasswordResetError,
    UpdatePasswordError
}