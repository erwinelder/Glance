package com.ataglance.walletglance.ui.utils

import com.ataglance.walletglance.domain.entities.Category
import com.ataglance.walletglance.data.categories.CategoriesLists
import com.ataglance.walletglance.data.categories.ParentCategoriesLists
import com.ataglance.walletglance.data.categories.SubcategoriesLists
import com.ataglance.walletglance.data.records.RecordType


fun List<Category>.getIdsThatAreNotInList(list: List<Category>): List<Int> {
    return this
        .filter { list.findById(it.id) == null }
        .map { it.id }
}


fun List<Category>.getOrderNumById(id: Int): Int? {
    return this.find { it.id == id }?.orderNum
}


fun List<Category>.findById(id: Int): Category? {
    return this.find { it.id == id }
}

fun List<Category>.findByRecordNum(orderNum: Int): Category? {
    return this.find { it.orderNum == orderNum }
}


fun List<List<Category>>.getSubcategoryByIdAndParentOrderNum(
    subcategoryId: Int,
    parentCategoryOrderNum: Int
): Category? {
    return this.getOrNull(parentCategoryOrderNum - 1)?.find { it.id == subcategoryId }
}


fun getCategoryAndIconRes(
    categoriesLists: CategoriesLists,
    categoryNameAndIconMap: Map<String, Int>,
    categoryId: Int,
    subcategoryId: Int?,
    recordType: RecordType?
): Pair<Category?, Int?>? {
    val parentCategoryList = if (recordType == RecordType.Expense) {
        categoriesLists.parentCategories.expense
    } else {
        categoriesLists.parentCategories.income
    }

    return if (subcategoryId != null) {
        parentCategoryList.getOrderNumById(categoryId)?.let { parCategoryOrderNum ->
            recordType
                ?.let { categoriesLists.subcategories.getByType(recordType) }
                ?.getSubcategoryByIdAndParentOrderNum(subcategoryId, parCategoryOrderNum)
                ?.let { subcategory ->
                    subcategory to categoryNameAndIconMap[subcategory.iconName]
                }
        }
    } else {
        parentCategoryList.findById(categoryId)?.let { category ->
            category to categoryNameAndIconMap[category.iconName]
        }
    }
}


fun List<Category>.breakOnDifferentLists(): CategoriesLists {
    val parentCategoriesLists = this.extractParentCategoriesLists()
    val subcategoriesLists = this.extractSubcategoriesLists(parentCategoriesLists)

    return CategoriesLists(parentCategoriesLists, subcategoriesLists)
}


fun List<Category>.extractParentCategoriesLists(): ParentCategoriesLists {
    val expenseCategoriesList = mutableListOf<Category>()
    val incomeCategoriesList = mutableListOf<Category>()

    this.forEach { category ->
        if (category.isParentCategory()) {
            if (category.isExpense()) {
                expenseCategoriesList.add(category)
            } else if (category.isIncome()) {
                incomeCategoriesList.add(category)
            }
        }
    }
    expenseCategoriesList.sortBy { it.orderNum }
    incomeCategoriesList.sortBy { it.orderNum }

    return ParentCategoriesLists(expenseCategoriesList, incomeCategoriesList)
}


fun List<Category>.extractSubcategoriesLists(
    parentCategoriesLists: ParentCategoriesLists
): SubcategoriesLists {
    val expenseSubcategoriesList = mutableListOf<Category>()
    val incomeSubcategoriesList = mutableListOf<Category>()

    this.forEach { category ->
        if (category.isSubcategory()) {
            if (category.isExpense()) {
                expenseSubcategoriesList.add(category)
            } else {
                incomeSubcategoriesList.add(category)
            }
        }
    }
    expenseSubcategoriesList.sortBy { it.orderNum }
    incomeSubcategoriesList.sortBy { it.orderNum }

    return SubcategoriesLists(
        expenseSubcategoriesList.toTwoDimensionList(parentCategoriesLists.expense),
        incomeSubcategoriesList.toTwoDimensionList(parentCategoriesLists.income)
    )
}


private fun List<Category>.toTwoDimensionList(
    parentCategoryList: List<Category>
): List<List<Category>> {
    val twoDimenList = mutableListOf<MutableList<Category>>()

    repeat(parentCategoryList.size) {
        twoDimenList.add(mutableListOf())
    }

    this.forEach { subcategory ->
        subcategory.parentCategoryId?.let { parentCategoryId ->
            parentCategoryList.getOrderNumById(parentCategoryId)?.let {
                twoDimenList[it - 1].add(subcategory)
            }
        }
    }

    twoDimenList.forEach { categoryList ->
        if (!categoryList.checkCategoriesOrderNumbers()) {
            throw IllegalAccessException("Subcategories order numbers are not correct")
        }
    }

    return twoDimenList
}


private fun List<Category>.checkCategoriesOrderNumbers(): Boolean {
    this.sortedBy { it.orderNum }.forEachIndexed { index, category ->
        if (category.orderNum != index + 1) {
            return false
        }
    }
    return true
}


fun List<Category>.fixOrderNumbers(): List<Category> {
    val fixedCategoryList = mutableListOf<Category>()

    val categoryListsExpenseAndIncome = this.partition { it.isExpense() }
    val expenseCategoryListsParAndSub =
        categoryListsExpenseAndIncome.first.partition { it.isParentCategory() }
    val incomeCategoryListsParAndSub =
        categoryListsExpenseAndIncome.second.partition { it.isParentCategory() }

    fixedCategoryList + expenseCategoryListsParAndSub.first.getWithFixedOrderNumbers()
    fixedCategoryList + incomeCategoryListsParAndSub.first.getWithFixedOrderNumbers()

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

private fun List<Category>.getWithFixedOrderNumbers(): List<Category> {
    return this.sortedBy { it.orderNum }
        .mapIndexed { index, category ->
            category.copy(orderNum = index + 1)
        }
}
