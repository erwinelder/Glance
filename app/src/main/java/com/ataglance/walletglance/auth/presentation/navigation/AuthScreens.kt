package com.ataglance.walletglance.auth.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface AuthScreens {

    @Serializable
    data object SignIn : AuthScreens

    @Serializable
    data object SignUp : AuthScreens

    @Serializable
    data object SignInSuccessful : AuthScreens

    @Serializable
    data object SignUpSuccessful : AuthScreens

}