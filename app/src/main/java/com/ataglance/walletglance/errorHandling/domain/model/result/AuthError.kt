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
    ReauthenticationError,
    SignUpEmailVerificationError,
    EmailVerificationError,
    EmailForPasswordResetError,
    PasswordResetError,
    UpdatePasswordError
}