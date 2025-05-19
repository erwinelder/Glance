package com.ataglance.walletglance.auth.domain.navigation

import kotlinx.serialization.Serializable

sealed interface AuthScreens {

    @Serializable
    data class SignIn(val email: String = "") : AuthScreens

    @Serializable
    data class SignUp(val email: String = "") : AuthScreens

    @Serializable
    data object SignUpEmailVerification : AuthScreens

    @Serializable
    data class FinishSignUp(val oobCode: String) : AuthScreens

    @Serializable
    data object Profile : AuthScreens

    @Serializable
    data object UpdateEmail : AuthScreens

    @Serializable
    data object EmailUpdateEmailVerification : AuthScreens

    @Serializable
    data class VerifyEmailUpdate(val oobCode: String) : AuthScreens

    @Serializable
    data object UpdatePassword : AuthScreens

    @Serializable
    data class RequestPasswordReset(val email: String = "") : AuthScreens

    @Serializable
    data class ResetPassword(val obbCode: String) : AuthScreens

    @Serializable
    data object DeleteAccount : AuthScreens

    @Serializable
    data object ManageSubscriptions : AuthScreens

}