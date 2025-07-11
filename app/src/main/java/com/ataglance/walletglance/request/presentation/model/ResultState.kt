package com.ataglance.walletglance.request.presentation.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Stable

sealed class ResultState {

    @Stable
    data class TitleState(
        @StringRes val titleRes: Int,
        @StringRes val messageRes: Int?
    ) : ResultState()

    @Stable
    data class MessageState(
        @StringRes val messageRes: Int
    ) : ResultState()

    @Stable
    data class ButtonState(
        @StringRes val titleRes: Int,
        @StringRes val messageRes: Int?,
        @StringRes val buttonTextRes: Int,
        @DrawableRes val buttonIconRes: Int?
    ) : ResultState()

}