package com.ataglance.walletglance.category.domain

data class CategoryWithSubcategoryByType(
    val expense: CategoryWithSubcategory?,
    val income: CategoryWithSubcategory?
) {

    fun getByType(type: CategoryType): CategoryWithSubcategory? {
        return when (type) {
            CategoryType.Expense -> expense
            CategoryType.Income -> income
        }
    }

}
