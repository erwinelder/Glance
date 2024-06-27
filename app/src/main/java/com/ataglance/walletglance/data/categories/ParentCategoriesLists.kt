package com.ataglance.walletglance.data.categories

data class ParentCategoriesLists(
    val expense: List<Category> = emptyList(),
    val income: List<Category> = emptyList()
) {

    fun getByType(type: CategoryType): List<Category> {
        return if (type == CategoryType.Expense) expense else income
    }

}