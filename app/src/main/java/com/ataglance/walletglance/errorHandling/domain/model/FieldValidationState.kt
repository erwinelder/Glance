package com.ataglance.walletglance.errorHandling.domain.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Stable

@Stable
data class FieldValidationState(
    val isValid: Boolean,
    @StringRes val messageRes: Int
)
