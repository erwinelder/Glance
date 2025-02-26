package com.ataglance.walletglance.category.domain.mapper

import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.category.domain.model.CategoryColor
import com.ataglance.walletglance.category.domain.model.GroupedCategories
import com.ataglance.walletglance.category.domain.model.GroupedCategoriesByType
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.color.ColorWithName


fun List<Category>.groupByType(): GroupedCategoriesByType {
    return GroupedCategoriesByType.fromGroupedCategories(this.group())
}

fun List<Category>.group(): List<GroupedCategories> {
    val (categories, subcategories) = partition { it.isParentCategory() }

    val subcategoryMap = subcategories
        .groupBy { it.parentCategoryId }
        .mapValues { entry ->
            entry.value.sortedBy { it.orderNum }
        }

    return categories.map { category ->
        GroupedCategories(
            category = category,
            subcategoryList = subcategoryMap[category.id].orEmpty()
        )
    }
}


fun CategoryColor.toColorWithName(theme: AppTheme): ColorWithName {
    return ColorWithName(this.name.name, this.color.getByTheme(theme).darker)
}