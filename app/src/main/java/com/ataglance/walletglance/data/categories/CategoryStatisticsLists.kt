package com.ataglance.walletglance.data.categories

data class CategoryStatisticsLists(
    val expense: List<CategoryStatisticsElementUiState> = emptyList(),
    val income: List<CategoryStatisticsElementUiState> = emptyList()
) {

    fun getByType(type: CategoryType): List<CategoryStatisticsElementUiState> {
        return when (type) {
            CategoryType.Expense -> expense
            CategoryType.Income -> income
        }
    }

    fun getItemByParentCategoryId(id: Int): CategoryStatisticsElementUiState? {
        return (expense + income).find { it.categoryId == id }
    }

}