package com.ataglance.walletglance.category.presentation.model

import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.category.domain.model.CategoryType

data class CheckedGroupedCategoriesByType(
    val expense: List<CheckedGroupedCategories> = emptyList(),
    val income: List<CheckedGroupedCategories> = emptyList()
) {

    fun concatenateLists(): List<CheckedGroupedCategories> {
        return if (expense.isNotEmpty() && income.isNotEmpty()) {
            expense + income
        } else if (income.isEmpty()) {
            expense
        } else {
            income
        }
    }

    private fun getByType(type: CategoryType): List<CheckedGroupedCategories> {
        return when (type) {
            CategoryType.Expense -> expense
            CategoryType.Income -> income
        }
    }

    private fun replaceListByType(
        list: List<CheckedGroupedCategories>,
        type: CategoryType
    ): CheckedGroupedCategoriesByType {
        return when (type) {
            CategoryType.Expense -> this.copy(expense = list)
            CategoryType.Income -> this.copy(income = list)
        }
    }


    fun inverseCheckedCategoryState(category: Category): CheckedGroupedCategoriesByType {
        return getByType(category.type)
            .let { list ->
                if (category.isParentCategory()) {
                    list.inverseCheckedParentCategoryState(category)
                } else {
                    list.inverseCheckedSubcategoryState(category)
                }
            }
            .let { newList ->
                replaceListByType(newList, category.type)
            }
    }

    private fun List<CheckedGroupedCategories>.inverseCheckedParentCategoryState(
        category: Category
    ): List<CheckedGroupedCategories> {
        return this.map { item ->
            item.takeIf { it.category.id != category.id } ?: item.inverseCheckedState()
        }
    }

    private fun List<CheckedGroupedCategories>.inverseCheckedSubcategoryState(
        category: Category
    ): List<CheckedGroupedCategories> {
        return this.map { item ->
            if (item.category.id != category.parentCategoryId) {
                item
            } else {
                val newSubcategoryList = item.subcategoryList.map { checkedCategory ->
                    checkedCategory.takeIf { checkedCategory.category.id != category.id }
                        ?: checkedCategory.inverseCheckedState()
                }
                val checkedUncheckedSubcategories = newSubcategoryList.partition { it.checked }

                if (checkedUncheckedSubcategories.first.isEmpty()) {
                    item.copy(checked = false, subcategoryList = newSubcategoryList)
                } else if (checkedUncheckedSubcategories.second.isEmpty()) {
                    item.copy(checked = true, subcategoryList = newSubcategoryList)
                } else {
                    item.copy(checked = null, subcategoryList = newSubcategoryList)
                }
            }
        }
    }


    fun inverseExpandedState(category: Category): CheckedGroupedCategoriesByType {
        return getByType(category.type).map { item ->
            item.takeIf { it.category.id != category.id } ?: item.copy(expanded = !item.expanded)
        }.let { newList ->
            replaceListByType(newList, category.type)
        }
    }


    fun hasCheckedCategory(): Boolean {
        return (expense.find { it.checked != false } ?: income.find { it.checked != false }) != null
    }

    fun getCheckedCategories(): List<Category> {
        (expense + income).let { list ->
            val (withoutSubcategories, withSubcategories) = list
                .partition { it.subcategoryList.isEmpty() }
            val parentCategories = withoutSubcategories
                .filter { it.checked != false }
                .map { it.category }
            val subcategories = withSubcategories.flatMap { item ->
                item.subcategoryList.filter { it.checked }.map { it.category }
            }
            return parentCategories + subcategories
        }
    }

}
