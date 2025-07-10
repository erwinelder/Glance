package com.ataglance.walletglance.request.presentation.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Stable

@Stable
data class ResultWithButtonState(
    val isSuccessful: Boolean,
    @StringRes val titleRes: Int,
    @StringRes val messageRes: Int?,
    @StringRes val buttonTextRes: Int,
    @DrawableRes val buttonIconRes: Int?
)
