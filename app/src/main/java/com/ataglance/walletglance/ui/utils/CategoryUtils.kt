package com.ataglance.walletglance.ui.utils

import com.ataglance.walletglance.data.app.AppTheme
import com.ataglance.walletglance.data.categories.CategoriesLists
import com.ataglance.walletglance.data.categories.Category
import com.ataglance.walletglance.data.categories.CategoryRank
import com.ataglance.walletglance.data.categories.CategoryType
import com.ataglance.walletglance.data.categories.ParentCategoriesLists
import com.ataglance.walletglance.data.categories.SubcategoriesLists
import com.ataglance.walletglance.data.categories.color.CategoryColorWithName
import com.ataglance.walletglance.data.categories.color.CategoryColors
import com.ataglance.walletglance.data.categories.color.CategoryPossibleColors
import com.ataglance.walletglance.data.categories.icons.CategoryPossibleIcons
import com.ataglance.walletglance.data.color.ColorWithName
import com.ataglance.walletglance.domain.entities.CategoryEntity


fun CategoryType.asChar(): Char {
    return if (this == CategoryType.Expense) '-' else '+'
}


fun CategoryRank.asChar(): Char {
    return if (this == CategoryRank.Parent) 'c' else 's'
}


fun CategoryColors.toCategoryColorWithName(): CategoryColorWithName {
    return CategoryColorWithName(this.name, this.color)
}

fun CategoryColors.toColorWithName(theme: AppTheme?): ColorWithName {
    return ColorWithName(this.name.name, this.color.getByTheme(theme).darker)
}


fun List<Category>.getOrderNumById(id: Int): Int? {
    return this.find { it.id == id }?.orderNum
}


fun List<Category>.findById(id: Int): Category? {
    return this.find { it.id == id }
}


fun List<CategoryEntity>.findById(id: Int): CategoryEntity? {
    return this.find { it.id == id }
}


fun List<Category>.findByRecordNum(orderNum: Int): Category? {
    return this.find { it.orderNum == orderNum }
}


fun List<CategoryEntity>.getIdsThatAreNotInList(list: List<CategoryEntity>): List<Int> {
    return this
        .filter { list.findById(it.id) == null }
        .map { it.id }
}


fun List<List<Category>>.getSubcategoryByIdAndParentOrderNum(
    subcategoryId: Int,
    parentCategoryOrderNum: Int
): Category? {
    return this.getOrNull(parentCategoryOrderNum - 1)?.find { it.id == subcategoryId }
}


fun List<Category>.toCategoryEntityList(): List<CategoryEntity> {
    return this.map { it.toCategoryEntity() }
}


fun List<CategoryEntity>.breakOnDifferentLists(): CategoriesLists {
    val parentCategoriesLists = this.extractParentCategoriesLists()
    val subcategoriesLists = this.extractSubcategoriesLists(parentCategoriesLists)

    return CategoriesLists(parentCategoriesLists, subcategoriesLists)
}


fun List<CategoryEntity>.extractParentCategoriesLists(): ParentCategoriesLists {
    val expenseCategoriesList = mutableListOf<Category>()
    val incomeCategoriesList = mutableListOf<Category>()

    val possibleIcons = CategoryPossibleIcons()
    val possibleColors = CategoryPossibleColors()

    this.forEach { categoryEntity ->
        if (categoryEntity.isParentCategory()) {
            val category = categoryEntity.toCategory(
                icon = possibleIcons.getIconByName(categoryEntity.iconName),
                color = possibleColors.getByName(categoryEntity.colorName)
            )
            if (categoryEntity.isExpense()) {
                expenseCategoriesList.add(category)
            } else if (categoryEntity.isIncome()) {
                incomeCategoriesList.add(category)
            }
        }
    }

    expenseCategoriesList.sortBy { it.orderNum }
    incomeCategoriesList.sortBy { it.orderNum }

    return ParentCategoriesLists(expenseCategoriesList, incomeCategoriesList)
}


fun List<CategoryEntity>.extractSubcategoriesLists(
    parentCategoriesLists: ParentCategoriesLists
): SubcategoriesLists {
    val expenseSubcategoriesList = mutableListOf<Category>()
    val incomeSubcategoriesList = mutableListOf<Category>()

    val possibleIcons = CategoryPossibleIcons()
    val possibleColors = CategoryPossibleColors()

    this.forEach { categoryEntity ->
        if (categoryEntity.isSubcategory()) {
            val category = categoryEntity.toCategory(
                icon = possibleIcons.getIconByName(categoryEntity.iconName),
                color = possibleColors.getByName(categoryEntity.colorName)
            )
            if (categoryEntity.isExpense()) {
                expenseSubcategoriesList.add(category)
            } else if (categoryEntity.isIncome()) {
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
