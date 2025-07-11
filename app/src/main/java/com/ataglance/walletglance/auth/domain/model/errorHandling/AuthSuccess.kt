package com.ataglance.walletglance.auth.domain.model.errorHandling

import com.ataglance.walletglance.request.domain.model.result.Success

enum class AuthSuccess : Success {
    SignedIn,
    SignUpEmailVerificationSent,
    SignUpVerificationCodeReceived,
    SignedUp,
    EmailUpdateEmailVerificationSent,
    EmailUpdateVerificationCodeReceived,
    EmailUpdated,
    ResetPasswordEmailSent,
    PasswordUpdated,
    AccountDeleted
}