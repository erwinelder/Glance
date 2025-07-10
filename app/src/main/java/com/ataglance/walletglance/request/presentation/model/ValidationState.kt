package com.ataglance.walletglance.request.presentation.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Stable

@Stable
data class ValidationState(
    val isValid: Boolean,
    @StringRes val messageRes: Int
)
