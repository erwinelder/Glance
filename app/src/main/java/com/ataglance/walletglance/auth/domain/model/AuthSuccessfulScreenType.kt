package com.ataglance.walletglance.auth.domain.model

import androidx.annotation.StringRes
import com.ataglance.walletglance.R

data class AuthSuccessfulScreenType(
    val type: AuthSuccessfulScreenTypeEnum,
    private val isAppSetUp: Boolean
) {

    companion object {
        fun fromString(type: String, isAppSetUp: Boolean): AuthSuccessfulScreenType {
            return AuthSuccessfulScreenType(
                type = when (type) {
                    AuthSuccessfulScreenTypeEnum.AfterSignIn.name -> AuthSuccessfulScreenTypeEnum.AfterSignIn
                    else -> AuthSuccessfulScreenTypeEnum.AfterEmailVerification
                },
                isAppSetUp = isAppSetUp
            )
        }
    }

    @StringRes fun getScreenTitleRes(): Int {
        return when (type) {
            AuthSuccessfulScreenTypeEnum.AfterSignIn -> R.string.welcome_back_to_glance
            AuthSuccessfulScreenTypeEnum.AfterEmailVerification -> when (isAppSetUp) {
                true -> R.string.all_set
                false -> R.string.welcome_to_glance
            }
        }
    }

    @StringRes fun getPrimaryButtonTextRes(): Int {
        return when (type) {
            AuthSuccessfulScreenTypeEnum.AfterSignIn -> R.string.continue_to_app
            AuthSuccessfulScreenTypeEnum.AfterEmailVerification -> when (isAppSetUp) {
                true -> R.string.continue_to_app
                false -> R.string.continue_setup
            }
        }
    }

}
