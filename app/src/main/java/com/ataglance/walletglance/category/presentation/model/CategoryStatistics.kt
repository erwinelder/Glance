package com.ataglance.walletglance.category.presentation.model

import com.ataglance.walletglance.category.domain.model.CategoriesStatsItem
import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.core.utils.formatWithSpaces
import com.ataglance.walletglance.core.utils.roundToTwoDecimals

data class CategoryStatistics(
    val category: Category,
    val totalAmount: String,
    val currency: String,
    val percentageFormatted: String,
    val percentageFloat: Float,
    val subcategoriesStatistics: List<CategoryStatistics>? = null
) {

    companion object {

        fun fromStatsMap(
            accountCurrency: String,
            allCategoriesTotalAmount: Double,
            categoriesStatsItem: CategoriesStatsItem,
            subcategoriesStatistics: MutableMap<Int, CategoriesStatsItem>? = null
        ): CategoryStatistics {
            val percentage = categoriesStatsItem.getPercentage(allCategoriesTotalAmount)

            return CategoryStatistics(
                category = categoriesStatsItem.category,
                totalAmount = categoriesStatsItem.totalAmount.formatWithSpaces(),
                currency = accountCurrency,
                percentageFormatted = percentage.roundToTwoDecimals(suffix = "%"),
                percentageFloat = percentage / 100,
                subcategoriesStatistics = subcategoriesStatistics?.values
                    ?.sortedByDescending { it.totalAmount }
                    ?.map {
                        fromStatsMap(
                            accountCurrency = accountCurrency,
                            allCategoriesTotalAmount = categoriesStatsItem.totalAmount,
                            categoriesStatsItem = it
                        )
                    }
            )
        }


        fun fromStats(
            category: Category,
            totalAmount: Double,
            generalTotalAmount: Double,
            accountCurrency: String,
            subcategoriesStats: List<CategoryStatistics>? = null
        ): CategoryStatistics {
            val percentage = if (generalTotalAmount != 0.0) {
                (100 / generalTotalAmount * totalAmount).toFloat()
            } else 0.0f

            return CategoryStatistics(
                category = category,
                totalAmount = totalAmount.formatWithSpaces(),
                currency = accountCurrency,
                percentageFormatted = percentage.roundToTwoDecimals(suffix = "%"),
                percentageFloat = percentage / 100,
                subcategoriesStatistics = subcategoriesStats
            )
        }

    }

}