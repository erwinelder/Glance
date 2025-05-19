package com.ataglance.walletglance.auth.domain.model.errorHandling

import com.ataglance.walletglance.errorHandling.domain.model.result.Success

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