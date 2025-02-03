package com.ataglance.walletglance.category.domain.mapper

import com.ataglance.walletglance.category.domain.model.CategoriesWithSubcategories
import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.category.domain.model.CategoryWithSubcategories


fun List<Category>.toCategoriesWithSubcategories(): CategoriesWithSubcategories {
    val (categories, subcategories) = this.partition { it.isParentCategory() }

    val subcategoryMap = mutableMapOf<Int, MutableList<Category>>()
    subcategories.forEach { subcategory ->
        subcategory.parentCategoryId?.let { parentCategoryId ->
            if (subcategoryMap.containsKey(subcategory.parentCategoryId)) {
                subcategoryMap[parentCategoryId]!!.add(subcategory)
            } else {
                subcategoryMap[parentCategoryId] = mutableListOf(subcategory)
            }
        }
    }

    return categories
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