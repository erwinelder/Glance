package com.ataglance.walletglance.request.presentation.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Stable

@Stable
data class ResultTitleWithMessageState(
    val isSuccessful: Boolean,
    @StringRes val titleRes: Int,
    @StringRes val messageRes: Int?
)
