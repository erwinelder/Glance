package com.ataglance.walletglance.category.domain.model

data class CategoryStatisticsElementUiState(
    val category: Category,
    val totalAmount: String,
    val currency: String,
    val percentageFloat: Float,
    val percentageFormatted: String,
    val subcategoriesStatisticsUiState: List<CategoryStatisticsElementUiState>? = null
)