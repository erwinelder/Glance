package com.ataglance.walletglance.category.domain

import com.ataglance.walletglance.core.utils.formatWithSpaces

data class CategoriesStatsMapItem(
    val category: Category,
    var totalAmount: Double = 0.0
) {

    fun toCategoryStatisticsElementUiState(
        accountCurrency: String,
        allCategoriesTotalAmount: Double,
        subcategoriesStatistics: MutableMap<Int, CategoriesStatsMapItem>? = null
    ): CategoryStatisticsElementUiState {
        return CategoryStatisticsElementUiState(
            categoryId = category.id,
            categoryName = category.name,
            categoryIconRes = category.icon.res,
            categoryColor = category.colorWithName.color,
            totalAmount = totalAmount.formatWithSpaces(),
            currency = accountCurrency,
            percentage = getPercentage(allCategoriesTotalAmount),
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