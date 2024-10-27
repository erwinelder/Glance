package com.ataglance.walletglance.category.domain.model

import com.ataglance.walletglance.core.utils.formatWithSpaces
import java.util.Locale

data class CategoriesStatsMapItem(
    val category: Category,
    var totalAmount: Double = 0.0
) {

    fun toCategoryStatisticsElementUiState(
        accountCurrency: String,
        allCategoriesTotalAmount: Double,
        subcategoriesStatistics: MutableMap<Int, CategoriesStatsMapItem>? = null
    ): CategoryStatisticsElementUiState {
        val percentage = getPercentage(allCategoriesTotalAmount)

        return CategoryStatisticsElementUiState(
            category = category,
            totalAmount = totalAmount.formatWithSpaces(),
            currency = accountCurrency,
            percentageFloat = percentage / 100,
            percentageFormatted = "%.2f".format(Locale.US, percentage) + "%",
            subcategoriesStatisticsUiState = subcategoriesStatistics?.values
                ?.sortedByDescending { it.totalAmount }
                ?.map {
                    it.toCategoryStatisticsElementUiState(
                        accountCurrency = accountCurrency,
                        allCategoriesTotalAmount = totalAmount
                    )
                }
        )
    }

    private fun getPercentage(allCategoriesTotalAmount: Double): Float {
        return if (allCategoriesTotalAmount != 0.0) {
            ((100 / allCategoriesTotalAmount) * totalAmount).toFloat()
        } else {
            0.0f
        }
    }

}