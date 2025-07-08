package com.ataglance.walletglance.auth.domain.model.errorHandling

import com.ataglance.walletglance.errorHandling.domain.model.result.Error

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
    AccountNotDeleted
}