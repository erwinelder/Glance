package com.ataglance.walletglance.auth.presentation.utils

import com.ataglance.walletglance.auth.domain.model.SignInCase
import com.ataglance.walletglance.auth.presentation.navigation.AuthScreens

fun getAuthNavGraphStartDestination(
    isSignedIn: Boolean,
    signInCase: SignInCase = SignInCase.Default
): AuthScreens {
    return if (isSignedIn) AuthScreens.Profile else AuthScreens.SignIn(case = signInCase)
}