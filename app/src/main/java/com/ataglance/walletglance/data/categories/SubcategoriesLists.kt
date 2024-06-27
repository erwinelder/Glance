package com.ataglance.walletglance.data.categories

data class SubcategoriesLists(
    val expense: List<List<Category>> = emptyList(),
    val income: List<List<Category>> = emptyList()
) {

    fun getByType(type: CategoryType): List<List<Category>> {
        return if (type == CategoryType.Expense) expense else income
    }

}