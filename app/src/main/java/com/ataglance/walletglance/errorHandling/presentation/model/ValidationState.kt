package com.ataglance.walletglance.errorHandling.presentation.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Stable

@Stable
data class ValidationState(
    val isValid: Boolean,
    @StringRes val messageRes: Int
)
