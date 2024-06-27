package com.ataglance.walletglance.data.categories

data class CategoryStatisticsLists(
    val expense: List<CategoryStatisticsElementUiState> = emptyList(),
    val income: List<CategoryStatisticsElementUiState> = emptyList()
)