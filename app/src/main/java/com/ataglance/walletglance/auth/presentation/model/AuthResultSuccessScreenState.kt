package com.ataglance.walletglance.auth.presentation.model

import androidx.annotation.StringRes
import com.ataglance.walletglance.R
import com.ataglance.walletglance.auth.domain.model.AuthResultSuccessScreenType
import com.ataglance.walletglance.auth.domain.model.SignInCase
import com.ataglance.walletglance.auth.presentation.navigation.AuthScreens
import com.ataglance.walletglance.core.presentation.navigation.MainScreens
import com.ataglance.walletglance.core.utils.enumValueOrNull
import com.ataglance.walletglance.settings.navigation.SettingsScreens

data class AuthResultSuccessScreenState(
    val type: AuthResultSuccessScreenType,
    private val isAppSetUp: Boolean
) {

    companion object {
        fun fromString(type: String, isAppSetUp: Boolean): AuthResultSuccessScreenState {
            return AuthResultSuccessScreenState(
                type = enumValueOrNull<AuthResultSuccessScreenType>(type)
                    ?: AuthResultSuccessScreenType.SignIn,
                isAppSetUp = isAppSetUp
            )
        }
    }

    @StringRes fun getScreenTitleRes(): Int {
        return when (type) {
            AuthResultSuccessScreenType.SignIn -> R.string.welcome_back_to_glance
            AuthResultSuccessScreenType.EmailVerification -> when (isAppSetUp) {
                true -> R.string.all_set
                false -> R.string.welcome_to_glance
            }
            AuthResultSuccessScreenType.PasswordUpdate -> R.string.password_updated
            AuthResultSuccessScreenType.AccountDeletion -> R.string.account_deleted
        }
    }

    @StringRes fun getPrimaryButtonTextRes(): Int {
        return when (type) {
            AuthResultSuccessScreenType.SignIn -> R.string.continue_to_app
            AuthResultSuccessScreenType.EmailVerification,
            AuthResultSuccessScreenType.PasswordUpdate ->
                when (isAppSetUp) {
                    true -> R.string.continue_to_app
                    false -> R.string.continue_setup
                }
            AuthResultSuccessScreenType.AccountDeletion -> R.string.start_setup
        }
    }

    fun getNextScreenNavigateTo(): Any {
        return when (type) {
            AuthResultSuccessScreenType.SignIn -> when (isAppSetUp) {
                true -> AuthScreens.Profile
                false -> MainScreens.FinishSetup
            }
            AuthResultSuccessScreenType.EmailVerification -> when (isAppSetUp) {
                true -> AuthScreens.Profile
                false -> SettingsScreens.Accounts
            }
            AuthResultSuccessScreenType.PasswordUpdate -> when (isAppSetUp) {
                true -> AuthScreens.Profile
                false -> AuthScreens.SignIn(SignInCase.Default.name)
            }
            AuthResultSuccessScreenType.AccountDeletion ->
                SettingsScreens.Start
        }
    }

}
