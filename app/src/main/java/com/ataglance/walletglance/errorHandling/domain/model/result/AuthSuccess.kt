package com.ataglance.walletglance.errorHandling.domain.model.result

enum class AuthSuccess : Success {
    SignUpEmailVerificationSent,
    UpdateEmailEmailVerificationSent,
    EmailUpdated,
    PasswordUpdated,
    ResetPasswordEmailSent,
    AccountDeleted
}