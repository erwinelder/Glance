package com.ataglance.walletglance.core.domain.componentState

import androidx.annotation.StringRes
import com.ataglance.walletglance.R

data class SetupProgressTopBarUiState(
    val isVisible: Boolean = false,
    @StringRes val titleRes: Int = R.string.settings
)
