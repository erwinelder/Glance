package com.ataglance.walletglance.data.statistics

import androidx.compose.runtime.Stable

@Stable
data class ColumnChartItemUiState(
    val name: String,
    val popUpValue: String,
    val percentageOnGraph: Float
)
