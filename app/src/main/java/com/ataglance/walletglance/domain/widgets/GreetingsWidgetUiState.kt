package com.ataglance.walletglance.domain.widgets

import androidx.annotation.StringRes
import com.ataglance.walletglance.R

data class GreetingsWidgetUiState(
    @StringRes val titleRes: Int = R.string.greetings_empty_message,
    val expensesTotal: Double = 0.0
)