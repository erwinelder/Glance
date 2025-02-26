package com.ataglance.walletglance.category.domain.model

data class CategoryWithSubByType(
    val expense: CategoryWithSub? = null,
    val income: CategoryWithSub? = null
) {

    fun putByType(
        type: CategoryType,
        categoryWithSub: CategoryWithSub?
    ): CategoryWithSubByType {
        return when (type) {
            CategoryType.Expense -> copy(expense = categoryWithSub)
            CategoryType.Income -> copy(income = categoryWithSub)
        }
    }

    fun getByType(type: CategoryType): CategoryWithSub? {
        return when (type) {
            CategoryType.Expense -> expense
            CategoryType.Income -> income
        }
    }

}
