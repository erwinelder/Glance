package com.ataglance.walletglance.auth.presentation.mapper

import com.ataglance.walletglance.auth.domain.model.AuthResultSuccessScreenType
import com.ataglance.walletglance.auth.domain.model.SignInCase

fun SignInCase.toAuthResultSuccessScreenType(): AuthResultSuccessScreenType {
    return when (this) {
        SignInCase.Default -> AuthResultSuccessScreenType.SignIn
        SignInCase.EmailVerificationError -> AuthResultSuccessScreenType.EmailVerification
    }
}