package com.ataglance.walletglance.errorHandling.presentation.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Stable

@Stable
data class ResultUiState(
    val isSuccessful: Boolean,
    @StringRes val titleRes: Int,
    @StringRes val messageRes: Int
)
