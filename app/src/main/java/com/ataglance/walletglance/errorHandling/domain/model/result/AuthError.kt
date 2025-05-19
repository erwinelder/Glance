package com.ataglance.walletglance.errorHandling.domain.model.result

enum class AuthError : Error {
    UserNotSignedIn,
    RequestDataNotValid,
    SessionExpired,
    AppUpdateRequired,
    InvalidCredentials,
    ReauthenticationError,
    EmailNotVerified,
    UserNotFound,
    SignInError,
    UserAlreadyExists,
    SignUpEmailVerificationError,
    SignUpError,
    CheckEmailVerificationError,
    OobCodeIsInvalid,
    FinishSignUpError,
    RequestEmailUpdateError,
    EmailUpdateError,
    PasswordUpdateError,
    ResetPasswordRequestError,
    PasswordResetError,
    AccountNotDeleted,
    LanguageNotSaved
}