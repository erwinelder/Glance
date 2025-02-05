package com.ataglance.walletglance.category.domain.mapper

import com.ataglance.walletglance.category.domain.model.CategoriesWithSubcategories
import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.category.domain.model.CategoryColor
import com.ataglance.walletglance.category.domain.model.CategoryWithSubcategories
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.color.ColorWithName


fun List<Category>.toCategoriesWithSubcategories(): CategoriesWithSubcategories {
    val categoriesWithSubcategories = this.groupCategoriesWithSubcategories()

    return categoriesWithSubcategories
        .partition { it.category.isExpense() }
        .let { (expenseCategories, incomeCategories) ->
            CategoriesWithSubcategories(
                expense = expenseCategories.sortedBy { it.category.orderNum },
                income = incomeCategories.sortedBy { it.category.orderNum }
            )
        }
}

fun List<Category>.groupCategoriesWithSubcategories(): List<CategoryWithSubcategories> {
    val (categories, subcategories) = this.partition { it.isParentCategory() }

    val subcategoryMap = subcategories
        .groupBy { it.parentCategoryId }
        .mapValues { entry ->
            entry.value.sortedBy { it.orderNum }
        }

    return categories.map { category ->
        CategoryWithSubcategories(
            category = category,
            subcategoryList = subcategoryMap[category.id].orEmpty()
        )
    }
}


fun CategoryColor.toColorWithName(theme: AppTheme): ColorWithName {
    return ColorWithName(this.name.name, this.color.getByTheme(theme).darker)
}