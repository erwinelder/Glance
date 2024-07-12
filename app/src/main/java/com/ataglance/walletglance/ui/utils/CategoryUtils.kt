package com.ataglance.walletglance.ui.utils

import com.ataglance.walletglance.data.app.AppTheme
import com.ataglance.walletglance.data.categories.CategoriesWithSubcategories
import com.ataglance.walletglance.data.categories.Category
import com.ataglance.walletglance.data.categories.CategoryRank
import com.ataglance.walletglance.data.categories.CategoryType
import com.ataglance.walletglance.data.categories.CategoryWithSubcategories
import com.ataglance.walletglance.data.categories.CheckedCategory
import com.ataglance.walletglance.data.categories.EditingCategoryWithSubcategories
import com.ataglance.walletglance.data.categories.color.CategoryColorWithName
import com.ataglance.walletglance.data.categories.color.CategoryColors
import com.ataglance.walletglance.data.categories.color.CategoryPossibleColors
import com.ataglance.walletglance.data.categories.icons.CategoryPossibleIcons
import com.ataglance.walletglance.data.color.ColorWithName
import com.ataglance.walletglance.domain.entities.CategoryEntity


fun CategoryType.asChar(): Char {
    return if (this == CategoryType.Expense) '-' else '+'
}


fun CategoryType.toggle(): CategoryType {
    return when (this) {
        CategoryType.Expense -> CategoryType.Income
        CategoryType.Income -> CategoryType.Expense
    }
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


fun List<CategoryEntity>.toCategoryList(): List<Category> {
    val possibleIcons = CategoryPossibleIcons()
    val possibleColors = CategoryPossibleColors()
    return this.map {
        it.toCategory(
            icon = possibleIcons.getIconByName(it.iconName),
            color = possibleColors.getByName(it.colorName)
        )
    }
}


fun List<Category>.findById(id: Int): Category? {
    return this.find { it.id == id }
}


fun List<CategoryEntity>.findById(id: Int): CategoryEntity? {
    return this.find { it.id == id }
}


fun List<CategoryEntity>.getIdsThatAreNotInList(list: List<CategoryEntity>): List<Int> {
    return this
        .filter { list.findById(it.id) == null }
        .map { it.id }
}


fun List<Category>.toCategoryEntityList(): List<CategoryEntity> {
    return this.map { it.toCategoryEntity() }
}


fun List<CategoryEntity>.toCategoriesWithSubcategories(): CategoriesWithSubcategories {
    val categoriesAndSubcategories = this.toCategoryList().partition { it.isParentCategory() }

    val subcategoryMap = mutableMapOf<Int, MutableList<Category>>()
    categoriesAndSubcategories.second.forEach { subcategory ->
        subcategory.parentCategoryId?.let { parentCategoryId ->
            if (subcategoryMap.containsKey(subcategory.parentCategoryId)) {
                subcategoryMap[parentCategoryId]!!.add(subcategory)
            } else {
                subcategoryMap[parentCategoryId] = mutableListOf(subcategory)
            }
        }
    }

    return categoriesAndSubcategories.first
        .map { category ->
            CategoryWithSubcategories(
                category = category,
                subcategoryList = subcategoryMap[category.id]?.sortedBy { it.orderNum }
                    ?: emptyList(),
            )
        }
        .partition { it.category.isExpense() }
        .let { expenseAndIncomeCategoriesWithSubcategories ->
            CategoriesWithSubcategories(
                expense = expenseAndIncomeCategoriesWithSubcategories.first
                    .sortedBy { it.category.orderNum },
                income = expenseAndIncomeCategoriesWithSubcategories.second
                    .sortedBy { it.category.orderNum }
            )
        }

}


fun List<Category>.toCheckedCategoryList(
    checkedCategoryList: List<Category>
): List<CheckedCategory> {
    return this.map { it.toCheckedCategory(checkedCategoryList) }
}


fun List<CategoryWithSubcategories>.toEditingCategoryWithSubcategoriesList(
    checkedCategoryList: List<Category>
): List<EditingCategoryWithSubcategories> {
    return this.map { it.toEditingCategoryWithSubcategories(checkedCategoryList) }
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
