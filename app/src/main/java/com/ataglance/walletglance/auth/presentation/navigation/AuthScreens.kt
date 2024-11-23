package com.ataglance.walletglance.auth.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface AuthScreens {

    @Serializable
    data object SignIn : AuthScreens

    @Serializable
    data object SignUp : AuthScreens

    @Serializable
    data class AuthSuccessful(val screenType: String) : AuthScreens

    @Serializable
    data object Profile : AuthScreens

    @Serializable
    data object UpdatePassword : AuthScreens

    @Serializable
    data object RequestPasswordReset : AuthScreens

    @Serializable
    data class ResetPassword(val obbCode: String) : AuthScreens

}