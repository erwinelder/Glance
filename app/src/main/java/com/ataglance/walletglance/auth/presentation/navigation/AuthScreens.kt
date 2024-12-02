package com.ataglance.walletglance.auth.presentation.navigation

import com.ataglance.walletglance.auth.domain.model.SignInCase
import kotlinx.serialization.Serializable

sealed interface AuthScreens {

    @Serializable
    data class SignIn(val case: SignInCase) : AuthScreens

    @Serializable
    data object SignUp : AuthScreens

    @Serializable
    data object EmailVerificationFailed : AuthScreens

    @Serializable
    data class AuthSuccessful(val screenType: String) : AuthScreens

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
    data object PasswordUpdateSuccessful : AuthScreens

    @Serializable
    data object DeleteAccount : AuthScreens

}