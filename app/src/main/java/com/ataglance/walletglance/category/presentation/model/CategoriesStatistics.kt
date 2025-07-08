package com.ataglance.walletglance.category.presentation.model

import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.category.domain.model.GroupedCategories
import com.ataglance.walletglance.transaction.domain.model.Transaction
import com.ataglance.walletglance.transaction.domain.utils.groupTotalAmountsByCategories

data class CategoriesStatistics(
    val stats: List<CategoryStatistics> = emptyList()
) {

    companion object {

        fun fromTransactions(
            type: CategoryType,
            accountId: Int,
            accountCurrency: String,
            transactions: List<Transaction>,
            groupedCategories: List<GroupedCategories>
        ): CategoriesStatistics {
            val rawStats = transactions.groupTotalAmountsByCategories(
                type = type, accountId = accountId
            )

            return CategoriesStatistics(
                stats = getCategoryStatisticsByType(
                    rawStats = rawStats,
                    accountCurrency = accountCurrency,
                    groupedCategories = groupedCategories
                )
            )
        }

        private fun getCategoryStatisticsByType(
            rawStats: Map<Int, Map<Int?, Double>>,
            accountCurrency: String,
            groupedCategories: List<GroupedCategories>
        ): List<CategoryStatistics> {
            val generalTotalAmount = rawStats.values.sumOf { map -> map.values.sum() }

            return rawStats.mapNotNull { (categoryId, subcategoryIdToAmountMap) ->
                val categoryTotalAmount = subcategoryIdToAmountMap.values.sum()
                val categoryWithSub = groupedCategories.find { it.categoryId == categoryId }
                    ?: return@mapNotNull null

                val subcategoriesStats = subcategoryIdToAmountMap
                    .mapNotNull { (subcategoryId, subcategoryTotalAmount) ->
                        val subcategory = categoryWithSub.getSubcategoryById(id = subcategoryId)
                            ?: return@mapNotNull null

                        CategoryStatistics.fromStats(
                            category = subcategory,
                            totalAmount = subcategoryTotalAmount,
                            generalTotalAmount = categoryTotalAmount,
                            accountCurrency = accountCurrency
                        )
                    }

                CategoryStatistics.fromStats(
                    category = categoryWithSub.category,
                    totalAmount = categoryTotalAmount,
                    generalTotalAmount = generalTotalAmount,
                    accountCurrency = accountCurrency,
                    subcategoriesStats = subcategoriesStats
                )
            }
        }

    }


    fun getParentStatsIfSubStatsPresent(categoryId: Int): CategoryStatistics? {
        return stats
            .find { it.category.id == categoryId }
            ?.takeIf { it.subcategoriesStatistics != null }
    }

}