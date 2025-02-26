package com.ataglance.walletglance.category.presentation.model

import com.ataglance.walletglance.category.domain.model.CategoriesStatsItem
import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.core.utils.formatWithSpaces
import com.ataglance.walletglance.core.utils.roundToTwoDecimals

data class CategoryStatistics(
    val category: Category,
    val totalAmount: String,
    val currency: String,
    val percentageFloat: Float,
    val percentageFormatted: String,
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
                percentageFloat = percentage / 100,
                percentageFormatted = percentage.roundToTwoDecimals(suffix = "%"),
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

    }

}