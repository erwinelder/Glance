package com.ataglance.walletglance.auth.domain.model

import androidx.annotation.StringRes
import com.ataglance.walletglance.R

data class AuthenticationSuccessfulScreenType(val type: ProfileScreenTypeEnum) {

    companion object {
        fun fromString(type: String): AuthenticationSuccessfulScreenType {
            return AuthenticationSuccessfulScreenType(
                type = when (type) {
                    ProfileScreenTypeEnum.AfterSignIn.name -> ProfileScreenTypeEnum.AfterSignIn
                    else -> ProfileScreenTypeEnum.AfterSignUp
                }
            )
        }
    }

    @StringRes fun getProfileScreenTitleRes(): Int {
        return when (type) {
            ProfileScreenTypeEnum.AfterSignIn -> R.string.welcome_back_to_glance
            ProfileScreenTypeEnum.AfterSignUp -> R.string.welcome_to_glance
        }
    }

    @StringRes fun getProfileScreenPrimaryButtonTextRes(): Int {
        return when (type) {
            ProfileScreenTypeEnum.AfterSignIn -> R.string.continue_to_app
            ProfileScreenTypeEnum.AfterSignUp -> R.string.continue_setup
        }
    }

}
