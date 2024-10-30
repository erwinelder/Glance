package com.ataglance.walletglance.category.data.utils

import com.ataglance.walletglance.category.data.model.CategoryEntity


fun List<CategoryEntity>.findById(id: Int): CategoryEntity? {
    return this.find { it.id == id }
}

fun List<CategoryEntity>.getThatAreNotInList(list: List<CategoryEntity>): List<CategoryEntity> {
    return this.filter { list.findById(it.id) == null }
}

fun List<CategoryEntity>.fixOrderNumbers(): List<CategoryEntity> {
    val fixedCategoryList = mutableListOf<CategoryEntity>()

    val (expenseCategoryList, incomeCategoryList) = this.partition { it.isExpense() }
    val (expenseParentCategoryList, expenseSubcategoryList) = expenseCategoryList
        .partition { it.isParentCategory() }
    val (incomeParentCategoryList, incomeSubcategoryList) = incomeCategoryList
        .partition { it.isParentCategory() }

    fixedCategoryList.addAll(expenseParentCategoryList.getWithFixedOrderNumbers())
    fixedCategoryList.addAll(incomeParentCategoryList.getWithFixedOrderNumbers())

    expenseSubcategoryList.sortedBy { it.parentCategoryId }
        .forEach { subcategory ->
            if (
                fixedCategoryList.lastOrNull()?.isParentCategory() == true ||
                subcategory.parentCategoryId != fixedCategoryList.lastOrNull()?.parentCategoryId
            ) {
                fixedCategoryList.add(subcategory.copy(orderNum = 1))
            } else {
                fixedCategoryList.lastOrNull()?.let {
                    fixedCategoryList.add(subcategory.copy(orderNum = it.orderNum + 1))
                }
            }
        }

    incomeSubcategoryList.sortedBy { it.parentCategoryId }
        .forEach { subcategory ->
            if (
                fixedCategoryList.lastOrNull()?.isParentCategory() == true ||
                subcategory.parentCategoryId != fixedCategoryList.lastOrNull()?.parentCategoryId
            ) {
                fixedCategoryList.add(subcategory.copy(orderNum = 1))
            } else {
                fixedCategoryList.lastOrNull()?.let {
                    fixedCategoryList.add(subcategory.copy(orderNum = it.orderNum + 1))
                }
            }
        }

    return fixedCategoryList
}

private fun List<CategoryEntity>.getWithFixedOrderNumbers(): List<CategoryEntity> {
    return this.sortedBy { it.orderNum }
        .mapIndexed { index, category ->
            category.copy(orderNum = index + 1)
        }
}