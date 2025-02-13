package com.ataglance.walletglance.core.domain.statistics

import android.content.Context
import androidx.compose.runtime.Stable
import com.ataglance.walletglance.core.domain.date.RepeatingPeriod
import com.ataglance.walletglance.core.utils.formatWithSpaces
import com.ataglance.walletglance.core.utils.getAverage
import com.ataglance.walletglance.core.utils.getColumnNameForColumnChart
import kotlin.math.pow

@Stable
data class ColumnChartUiState(
    val columns: List<ColumnChartColumnUiState> = emptyList(),
    val columnsNames: List<String> = emptyList(),
    val rowsNames: List<String> = emptyList(),
    val averageValue: Double = 0.0
) {

    companion object {

        fun asAmountsByDateRanges(
            totalAmountsByRanges: List<TotalAmountInRange>,
            rowsCount: Int,
            repeatingPeriod: RepeatingPeriod,
            context: Context
        ): ColumnChartUiState {
            val maxAmount = totalAmountsByRanges.maxOfOrNull { it.totalAmount } ?: 0.0

            val rowsNumbers = getRowsNumbers(rowsCount = rowsCount, generalMaxAmount = maxAmount)
            val columnsNames = totalAmountsByRanges.map { amountByRange ->
                repeatingPeriod.getColumnNameForColumnChart(amountByRange.dateRange, context)
            }

            val columnChartItemUiStateList = getColumnsUiStates(
                totalAmountsByRanges = totalAmountsByRanges,
                maxAmountOnGraph = rowsNumbers.getOrNull(0)?.toDouble() ?: 0.0,
                generalMaxAmount = maxAmount
            )

            return ColumnChartUiState(
                columns = columnChartItemUiStateList,
                columnsNames = columnsNames,
                rowsNames = rowsNumbers.map(Int::formatWithSpaces),
                averageValue = columnChartItemUiStateList.map { it.value }.getAverage()
            )
        }

        private fun getRowsNumbers(rowsCount: Int, generalMaxAmount: Double): List<Int> {
            val stepDecimal = generalMaxAmount.toInt() / 4

            val stepDigitCountHalf = stepDecimal.toString().length / 2
            val multiplicationStep = 10.0.pow(stepDigitCountHalf.toDouble()).toInt()

            val roundedStep = stepDecimal / multiplicationStep * multiplicationStep +
                    multiplicationStep

            val rowsNumbers = mutableListOf<Int>()
            for (i in rowsCount - 1 downTo 1) {
                rowsNumbers.add(roundedStep * i)
            }
            rowsNumbers.add(0)

            return rowsNumbers
        }

        private fun getColumnsUiStates(
            totalAmountsByRanges: List<TotalAmountInRange>,
            maxAmountOnGraph: Double,
            generalMaxAmount: Double
        ): List<ColumnChartColumnUiState> {
            return totalAmountsByRanges.map { amountByRange ->
                val totalAmount = amountByRange.totalAmount
                ColumnChartColumnUiState(
                    value = totalAmount,
                    percentageOnGraph = ((100 / maxAmountOnGraph * totalAmount) / 100).toFloat(),
                    generalPercentage = 100 / generalMaxAmount * totalAmount
                )
            }
        }

    }

}
