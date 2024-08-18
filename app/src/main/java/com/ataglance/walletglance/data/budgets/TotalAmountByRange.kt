package com.ataglance.walletglance.data.budgets

import android.content.Context
import com.ataglance.walletglance.data.date.LongDateRange
import com.ataglance.walletglance.data.date.RepeatingPeriod
import com.ataglance.walletglance.data.statistics.ColumnChartItemUiState
import com.ataglance.walletglance.data.utils.formatWithSpaces
import com.ataglance.walletglance.data.utils.getColumnNameForColumnChart

data class TotalAmountByRange(
    val dateRange: LongDateRange,
    val totalAmount: Double
) {

    fun toColumnChartItemUiState(
        maxTotalAmount: Double,
        repeatingPeriod: RepeatingPeriod,
        context: Context
    ): ColumnChartItemUiState {
        return ColumnChartItemUiState(
            name = repeatingPeriod.getColumnNameForColumnChart(dateRange, context),
            popUpValue = totalAmount.formatWithSpaces(),
            percentageOnGraph = (100 / maxTotalAmount * totalAmount).toFloat()
        )
    }

}
