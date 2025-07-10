package com.ataglance.walletglance.request.presentation.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Stable

sealed class ResultState {

    @Stable
    data class TitleState(
        val isSuccessful: Boolean,
        @StringRes val titleRes: Int,
        @StringRes val messageRes: Int?
    ) : ResultState()

    @Stable
    data class MessageState(
        val isSuccessful: Boolean,
        @StringRes val messageRes: Int
    ) : ResultState()

    @Stable
    data class ButtonState(
        val isSuccessful: Boolean,
        @StringRes val titleRes: Int,
        @StringRes val messageRes: Int?,
        @StringRes val buttonTextRes: Int,
        @DrawableRes val buttonIconRes: Int?
    ) : ResultState()

}