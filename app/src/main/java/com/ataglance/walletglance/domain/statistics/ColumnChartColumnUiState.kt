package com.ataglance.walletglance.domain.statistics

import androidx.compose.runtime.Stable

@Stable
data class ColumnChartColumnUiState(
    val value: Double,
    val percentageOnGraph: Float,
    val generalPercentage: Double
)
