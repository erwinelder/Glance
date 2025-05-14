package com.ataglance.walletglance.errorHandling.domain.model.result

enum class AuthSuccess : Success {
    SignedIn,
    SignedUp,
    SignUpEmailVerificationSent,
    UpdateEmailEmailVerificationSent,
    EmailUpdated,
    PasswordUpdated,
    ResetPasswordEmailSent,
    AccountDeleted
}