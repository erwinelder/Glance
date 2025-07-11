package com.ataglance.walletglance.settings.mapper

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.ataglance.walletglance.R
import com.ataglance.walletglance.request.presentation.model.ResultState.ButtonState
import com.ataglance.walletglance.settings.error.SettingsError


fun SettingsError.toResultStateButton(): ButtonState {
    return ButtonState(
        titleRes = this.asTitleRes(),
        messageRes = this.asMessageResOrNull(),
        buttonTextRes = this.asButtonTextRes(),
        buttonIconRes = this.asButtonIconResOrNull()
    )
}


@StringRes private fun SettingsError.asTitleRes(): Int {
    return when (this) {
        SettingsError.LanguageNotSavedRemotely -> R.string.oops
    }
}

@StringRes private fun SettingsError.asMessageResOrNull(): Int? {
    return when (this) {
        SettingsError.LanguageNotSavedRemotely -> R.string.language_not_saved_remotely_message
    }
}

@StringRes private fun SettingsError.asButtonTextRes(): Int {
    return when (this) {
        SettingsError.LanguageNotSavedRemotely -> R.string.close
    }
}

@DrawableRes private fun SettingsError.asButtonIconResOrNull(): Int? {
    return when (this) {
        SettingsError.LanguageNotSavedRemotely -> R.drawable.close_icon
    }
}
