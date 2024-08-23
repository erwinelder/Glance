package com.ataglance.walletglance.domain.utils

import com.ataglance.walletglance.domain.app.AppTheme
import com.ataglance.walletglance.domain.categories.Category
import com.ataglance.walletglance.domain.categories.CategoryType
import com.ataglance.walletglance.domain.categories.CategoryWithSubcategories
import com.ataglance.walletglance.domain.categories.CategoryWithSubcategory
import com.ataglance.walletglance.domain.categories.CheckedCategory
import com.ataglance.walletglance.domain.categories.EditingCategoryWithSubcategories
import com.ataglance.walletglance.domain.categories.color.CategoryColorWithName
import com.ataglance.walletglance.domain.categories.color.CategoryColors
import com.ataglance.walletglance.domain.categories.color.CategoryPossibleColors
import com.ataglance.walletglance.domain.categories.icons.CategoryPossibleIcons
import com.ataglance.walletglance.domain.color.ColorWithName
import com.ataglance.walletglance.data.local.entities.CategoryEntity
import com.ataglance.walletglance.data.mappers.toCategoryList


fun CategoryType.asChar(): Char {
    return if (this == CategoryType.Expense) '-' else '+'
}


fun CategoryType.toggle(): CategoryType {
    return when (this) {
        CategoryType.Expense -> CategoryType.Income
        CategoryType.Income -> CategoryType.Expense
    }
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

    return this.toCategoryList(
        iconProvider = possibleIcons::getIconByName,
        colorProvider = possibleColors::getByName
    )
}


fun List<Category>.findById(id: Int): Category? {
    return this.find { it.id == id }
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


fun List<CategoryWithSubcategories>.findCategoryById(id: Int): Category? {
    this.forEach { categoryWithSubcategories ->
        categoryWithSubcategories.category.takeIf { it.id == id }?.let { return it }
            ?: categoryWithSubcategories.subcategoryList.findById(id)?.let { return it }
    }
    return null
}


fun List<CategoryWithSubcategories>.getCategoryWithSubcategoryById(
    id: Int
): CategoryWithSubcategory? {
    this.forEach { categoryWithSubcategories ->
        categoryWithSubcategories
            .takeIf { it.category.id == id }
            ?.let {
                return CategoryWithSubcategory(it.category)
            }
            ?: categoryWithSubcategories.getWithSubcategoryWithId(id)
                .let { categoryWithSubcategory ->
                    categoryWithSubcategory
                        .takeIf { it.subcategory != null }
                        ?.let { return it }
                }
    }
    return null
}


private fun List<Category>.checkCategoriesOrderNumbers(): Boolean {
    this.sortedBy { it.orderNum }.forEachIndexed { index, category ->
        if (category.orderNum != index + 1) {
            return false
        }
    }
    return true
}
