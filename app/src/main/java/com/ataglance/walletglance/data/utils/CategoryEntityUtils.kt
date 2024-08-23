package com.ataglance.walletglance.data.utils

import com.ataglance.walletglance.data.local.entities.CategoryEntity


fun List<CategoryEntity>.findById(id: Int): CategoryEntity? {
    return this.find { it.id == id }
}

fun List<CategoryEntity>.getIdsThatAreNotInList(list: List<CategoryEntity>): List<Int> {
    return this
        .filter { list.findById(it.id) == null }
        .map { it.id }
}

fun List<CategoryEntity>.fixOrderNumbers(): List<CategoryEntity> {
    val fixedCategoryList = mutableListOf<CategoryEntity>()

    val categoryListsExpenseAndIncome = this.partition { it.isExpense() }
    val expenseCategoryListsParAndSub = categoryListsExpenseAndIncome.first
        .partition { it.isParentCategory() }
    val incomeCategoryListsParAndSub = categoryListsExpenseAndIncome.second
        .partition { it.isParentCategory() }

    fixedCategoryList.addAll(expenseCategoryListsParAndSub.first.getWithFixedOrderNumbers())
    fixedCategoryList.addAll(incomeCategoryListsParAndSub.first.getWithFixedOrderNumbers())

    expenseCategoryListsParAndSub.second.sortedBy { it.parentCategoryId }
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

    incomeCategoryListsParAndSub.second.sortedBy { it.parentCategoryId }
        .forEach { subcategory ->
            if (
                fixedCategoryList.lastOrNull()?.isParentCategory() == true &&
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