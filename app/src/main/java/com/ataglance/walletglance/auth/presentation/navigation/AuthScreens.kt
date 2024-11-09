package com.ataglance.walletglance.auth.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface AuthScreens {

    @Serializable
    data object SignInWithEmailAndPassword : AuthScreens

}