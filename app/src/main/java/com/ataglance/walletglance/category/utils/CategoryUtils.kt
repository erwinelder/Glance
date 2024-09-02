package com.ataglance.walletglance.category.utils

import com.ataglance.walletglance.category.data.local.model.CategoryEntity
import com.ataglance.walletglance.category.data.mapper.toCategoryList
import com.ataglance.walletglance.category.domain.Category
import com.ataglance.walletglance.category.domain.CategoryType
import com.ataglance.walletglance.category.domain.CategoryWithSubcategories
import com.ataglance.walletglance.category.domain.CategoryWithSubcategory
import com.ataglance.walletglance.category.domain.CheckedCategory
import com.ataglance.walletglance.category.domain.EditingCategoryWithSubcategories
import com.ataglance.walletglance.category.domain.color.CategoryColorWithName
import com.ataglance.walletglance.category.domain.color.CategoryColors
import com.ataglance.walletglance.category.domain.color.CategoryPossibleColors
import com.ataglance.walletglance.category.domain.icons.CategoryPossibleIcons
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.color.ColorWithName
import com.ataglance.walletglance.record.domain.RecordType


fun CategoryType.asChar(): Char {
    return if (this == CategoryType.Expense) '-' else '+'
}


fun CategoryType.toRecordType(): RecordType {
    return when (this) {
        CategoryType.Expense -> RecordType.Expense
        CategoryType.Income -> RecordType.Income
    }
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
