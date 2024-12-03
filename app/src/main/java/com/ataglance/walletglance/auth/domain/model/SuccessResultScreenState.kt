package com.ataglance.walletglance.auth.domain.model

import androidx.annotation.StringRes
import com.ataglance.walletglance.R

data class SuccessResultScreenState(
    val type: SuccessResultScreenType,
    private val isAppSetUp: Boolean
) {

    companion object {
        fun fromString(type: String, isAppSetUp: Boolean): SuccessResultScreenState {
            return SuccessResultScreenState(
                type = SuccessResultScreenType.entries.find { it.name == type }
                    ?: SuccessResultScreenType.SignInSuccess,
                isAppSetUp = isAppSetUp
            )
        }
    }

    @StringRes fun getScreenTitleRes(): Int {
        return when (type) {
            SuccessResultScreenType.SignInSuccess -> R.string.welcome_back_to_glance
            SuccessResultScreenType.EmailVerificationSuccess -> when (isAppSetUp) {
                true -> R.string.all_set
                false -> R.string.welcome_to_glance
            }
            SuccessResultScreenType.PasswordUpdateSuccess -> R.string.password_updated
            SuccessResultScreenType.AccountDeletionSuccess -> R.string.account_deleted
        }
    }

    @StringRes fun getPrimaryButtonTextRes(): Int {
        return when (type) {
            SuccessResultScreenType.SignInSuccess -> R.string.continue_to_app
            SuccessResultScreenType.EmailVerificationSuccess,
            SuccessResultScreenType.PasswordUpdateSuccess ->
                when (isAppSetUp) {
                    true -> R.string.continue_to_app
                    false -> R.string.continue_setup
                }
            SuccessResultScreenType.AccountDeletionSuccess -> R.string.start_setup
        }
    }

}
