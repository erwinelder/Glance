package com.ataglance.walletglance.core.domain.widgets

import androidx.annotation.StringRes
import com.ataglance.walletglance.R

data class GreetingsWidgetUiState(
    @StringRes val titleRes: Int = R.string.greetings_empty_message,
    val todayExpensesByActiveAccount: Double = 0.0
)