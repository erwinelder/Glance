package com.ataglance.walletglance.category.presentation.model

import com.ataglance.walletglance.category.domain.model.CategoryType

data class CategoriesStatisticsWidgetUiState(
    val top1Category: CategoryStatistics? = null,
    val top2Category: CategoryStatistics? = null,
    val top3Category: CategoryStatistics? = null
) {

    companion object {

        fun fromStatistics(stats: List<CategoryStatistics>): CategoriesStatisticsWidgetUiState {
            return CategoriesStatisticsWidgetUiState(
                top1Category = stats.getOrNull(0),
                top2Category = stats.getOrNull(1),
                top3Category = stats.getOrNull(2)
            )
        }

    }


    fun isEmpty(): Boolean {
        return top1Category == null && top2Category == null && top3Category == null
    }

    fun getType(): CategoryType? {
        return top1Category?.category?.type
    }

}