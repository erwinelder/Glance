package com.ataglance.walletglance.auth.domain.model

import androidx.annotation.StringRes
import com.ataglance.walletglance.R

data class AuthSuccessfulScreenType(val type: AuthSuccessfulScreenTypeEnum) {

    companion object {
        fun fromString(type: String): AuthSuccessfulScreenType {
            return AuthSuccessfulScreenType(
                type = when (type) {
                    AuthSuccessfulScreenTypeEnum.AfterSignIn.name -> AuthSuccessfulScreenTypeEnum.AfterSignIn
                    else -> AuthSuccessfulScreenTypeEnum.AfterSignUp
                }
            )
        }
    }

    @StringRes fun getScreenTitleRes(): Int {
        return when (type) {
            AuthSuccessfulScreenTypeEnum.AfterSignIn -> R.string.welcome_back_to_glance
            AuthSuccessfulScreenTypeEnum.AfterSignUp -> R.string.welcome_to_glance
        }
    }

    @StringRes fun getPrimaryButtonTextRes(): Int {
        return when (type) {
            AuthSuccessfulScreenTypeEnum.AfterSignIn -> R.string.continue_to_app
            AuthSuccessfulScreenTypeEnum.AfterSignUp -> R.string.continue_setup
        }
    }

}
