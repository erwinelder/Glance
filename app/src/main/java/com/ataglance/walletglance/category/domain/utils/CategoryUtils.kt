package com.ataglance.walletglance.category.domain.utils

import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.category.domain.model.CategoryWithSub
import com.ataglance.walletglance.category.domain.model.GroupedCategories


fun List<Category>.findById(id: Int): Category? {
    return find { it.id == id }
}


fun List<GroupedCategories>.getCategoryWithSubcategoryById(id: Int): CategoryWithSub? {
    forEach { groupedCategories ->

        groupedCategories.category.takeIf { it.id == id }
            ?.let { return CategoryWithSub(category = it) }

        groupedCategories.getWithSubcategoryOrNull(id = id)?.let { return it }

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
