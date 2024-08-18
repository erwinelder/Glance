package com.ataglance.walletglance.data.statistics

import androidx.compose.runtime.Stable

@Stable
data class ColumnChartUiState(
    val columns: List<ColumnChartItemUiState> = emptyList(),
    val horizontalLinesNames: List<String> = emptyList(),
    val selectedColumnIndex: Int? = null
)
