package com.ataglance.walletglance.data.categories

import com.ataglance.walletglance.data.app.AppTheme
import java.util.Locale

data class CategoriesStatsMapItem(
    val category: Category,
    var totalAmount: Double = 0.0
) {

    fun toCategoryStatisticsElementUiState(
        appTheme: AppTheme?,
        accountCurrency: String,
        allCategoriesTotalAmount: Double,
        subcategoriesStatistics: MutableMap<Int, CategoriesStatsMapItem>? = null
    ): CategoryStatisticsElementUiState {
        return CategoryStatisticsElementUiState(
            categoryId = category.id,
            categoryName = category.name,
            categoryIconRes = category.icon.res,
            categoryColor = category.colorWithName.color,
            totalAmount = getFormattedTotalAmount(),
            currency = accountCurrency,
            percentage = getPercentage(allCategoriesTotalAmount),
            subcategoriesStatisticsUiState = subcategoriesStatistics?.values
                ?.sortedByDescending { it.totalAmount }
                ?.map {
                    it.toCategoryStatisticsElementUiState(
                        appTheme = appTheme,
                        accountCurrency = accountCurrency,
                        allCategoriesTotalAmount = totalAmount
                    )
                }
        )
    }

    private fun getFormattedTotalAmount(): String {
        return "%.2f".format(Locale.US, totalAmount)
    }

    private fun getPercentage(allCategoriesTotalAmount: Double): Float {
        return if (allCategoriesTotalAmount != 0.0) {
            ((100 / allCategoriesTotalAmount) * totalAmount).toFloat()
        } else {
            0.0f
        }
    }

}