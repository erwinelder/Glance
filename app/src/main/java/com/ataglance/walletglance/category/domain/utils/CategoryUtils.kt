package com.ataglance.walletglance.category.domain.utils

import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.category.domain.model.GroupedCategories
import com.ataglance.walletglance.category.domain.model.CategoryWithSub
import com.ataglance.walletglance.record.domain.model.RecordType


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


fun List<Category>.findById(id: Int): Category? {
    return this.find { it.id == id }
}


fun List<GroupedCategories>.getCategoryWithSubcategoryById(id: Int): CategoryWithSub? {
    this.forEach { categoryWithSubcategories ->
        categoryWithSubcategories
            .takeIf { it.category.id == id }
            ?.let {
                return CategoryWithSub(it.category)
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


fun translateCategories(
    defaultCategoriesInCurrLocale: List<Category>,
    defaultCategoriesInNewLocale: List<Category>,
    currCategoryList: List<Category>
): List<Category> {
    val namesToTranslateMap = currCategoryList
        .mapNotNull { currentCategory ->
            defaultCategoriesInCurrLocale.find { it.name == currentCategory.name }
                ?.let { defaultCurrentCategory ->
                    currentCategory.id to defaultCategoriesInNewLocale
                        .find { it.id == defaultCurrentCategory.id }!!.name
                }
        }
        .toMap()
    return currCategoryList.mapNotNull { currentCategory ->
        namesToTranslateMap[currentCategory.id]?.let { currentCategory.copy(name = it) }
    }
}


fun List<GroupedCategories>.fixParentOrderNums(): List<GroupedCategories> {
    return this.mapIndexed { index, categoryWithSubcategories ->
        categoryWithSubcategories.copy(
            category = categoryWithSubcategories.category.copy(orderNum = index + 1)
        )
    }
}
