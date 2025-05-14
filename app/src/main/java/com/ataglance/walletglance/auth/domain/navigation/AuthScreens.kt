package com.ataglance.walletglance.auth.domain.navigation

import kotlinx.serialization.Serializable

sealed interface AuthScreens {

    @Serializable
    data class SignIn(val case: String, val email: String = "") : AuthScreens

    @Serializable
    data class SignUp(val email: String = "") : AuthScreens

    @Serializable
    data object EmailVerification : AuthScreens

    @Serializable
    data class FinishSignUp(val oobCode: String) : AuthScreens

    @Serializable
    data class ResultSuccess(val screenType: String) : AuthScreens

    @Serializable
    data object EmailVerificationFailed : AuthScreens

    @Serializable
    data object Profile : AuthScreens

    @Serializable
    data object UpdateEmail : AuthScreens

    @Serializable
    data object UpdatePassword : AuthScreens

    @Serializable
    data object RequestPasswordReset : AuthScreens

    @Serializable
    data class ResetPassword(val obbCode: String) : AuthScreens

    @Serializable
    data object DeleteAccount : AuthScreens

    @Serializable
    data object ManageSubscriptions : AuthScreens

}