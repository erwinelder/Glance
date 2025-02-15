package com.ataglance.walletglance.category.presentation.model

data class GroupedCategoryStatistics(
    val parentCategory: CategoryStatistics? = null,
    val subcategories: List<CategoryStatistics> = emptyList()
)
