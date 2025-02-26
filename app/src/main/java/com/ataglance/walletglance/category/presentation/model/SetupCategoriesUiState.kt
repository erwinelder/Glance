package com.ataglance.walletglance.category.presentation.model

import com.ataglance.walletglance.category.domain.model.GroupedCategoriesByType
import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.category.domain.model.GroupedCategories

data class SetupCategoriesUiState(
    val categoryType: CategoryType = CategoryType.Expense,
    val groupedCategories: GroupedCategories? = null,
    val groupedCategoriesByType: GroupedCategoriesByType = GroupedCategoriesByType()
) {

    private fun getNewCategoryId(): Int {
        return ((groupedCategoriesByType.asList() +
                groupedCategories?.subcategoryList.orEmpty())
            .maxOfOrNull { it.id } ?: 0) + 1
    }

    fun saveCategory(category: Category): SetupCategoriesUiState {
        return if (groupedCategories == null) {
            saveParentCategory(category)
        } else {
            saveSubcategory(category)
        }
    }

    private fun saveParentCategory(category: Category): SetupCategoriesUiState {
        return this.copy(
            groupedCategoriesByType = if (category.id == 0) {
                groupedCategoriesByType.appendNewCategory(
                    category = category.copy(id = getNewCategoryId())
                )
            } else {
                groupedCategoriesByType.replaceCategory(category)
            }
        )
    }

    private fun saveSubcategory(category: Category): SetupCategoriesUiState {
        return this.copy(
            groupedCategories = if (category.id == 0) {
                groupedCategories?.appendNewSubcategory(
                    subcategory = category.copy(id = getNewCategoryId())
                )
            } else {
                groupedCategories?.replaceSubcategory(category)
            }
        )
    }

}