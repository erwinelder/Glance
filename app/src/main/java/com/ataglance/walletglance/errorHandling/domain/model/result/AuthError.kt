package com.ataglance.walletglance.errorHandling.domain.model.result

enum class AuthError : Error {
    DataNotValid,
    InvalidCredentials,
    EmailNotVerified,
    UserNotFound,
    SignInError,
    UserAlreadyExists,
    SignUpEmailVerificationError,
    SignUpError,
    CheckEmailVerificationError,
    OobCodeIsInvalid,
    FinishSignUpError,
    SessionExpired,

    UserNotCreated,
    UserDataNotSaved,
    UserNotSignedIn,
    InvalidEmail,
    InvalidCode,
    WrongCredentials,
    ReauthenticationError,
    EmailVerificationError,
    EmailForPasswordResetError,
    PasswordResetError,
    UpdatePasswordError,
    DataDeletionError,
    AccountDeletionError
}