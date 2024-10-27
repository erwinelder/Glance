package com.ataglance.walletglance.category.domain.model

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
