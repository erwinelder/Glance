package com.ataglance.walletglance.data.statistics

import androidx.compose.runtime.Stable

@Stable
data class ColumnChartColumnUiState(
    val value: Double,
    val percentageOnGraph: Float,
    val generalPercentage: Double
)
