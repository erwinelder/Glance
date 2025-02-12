package com.ataglance.walletglance.category.domain.model

data class CategoryWithSubcategoryByType(
    val expense: CategoryWithSubcategory? = null,
    val income: CategoryWithSubcategory? = null
) {

    fun putByType(
        type: CategoryType,
        categoryWithSubcategory: CategoryWithSubcategory?
    ): CategoryWithSubcategoryByType {
        return when (type) {
            CategoryType.Expense -> copy(expense = categoryWithSubcategory)
            CategoryType.Income -> copy(income = categoryWithSubcategory)
        }
    }

    fun getByType(type: CategoryType): CategoryWithSubcategory? {
        return when (type) {
            CategoryType.Expense -> expense
            CategoryType.Income -> income
        }
    }

}
