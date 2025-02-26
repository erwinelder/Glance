package com.ataglance.walletglance.category.domain.model

import com.ataglance.walletglance.category.domain.utils.fixParentOrderNums
import com.ataglance.walletglance.core.utils.deleteItemAndMoveOrderNum

data class GroupedCategoriesByType(
    val expense: List<GroupedCategories> = emptyList(),
    val income: List<GroupedCategories> = emptyList()
) {

    companion object {

        fun fromGroupedCategories(groups: List<GroupedCategories>): GroupedCategoriesByType {
            return groups.partition { it.category.isExpense() }.let { (expense, income) ->
                GroupedCategoriesByType(
                    expense = expense.sortedBy { it.category.orderNum },
                    income = income.sortedBy { it.category.orderNum }
                )
            }
        }

    }


    fun asList(): List<Category> {
        return (expense + income).flatMap { categoryWithSubcategories ->
            categoryWithSubcategories.asSingleList()
        }
    }

    private fun getByTypeOrAll(type: CategoryType?): List<GroupedCategories> {
        return when (type) {
            CategoryType.Expense -> expense
            CategoryType.Income -> income
            else -> expense + income
        }
    }

    fun getByType(type: CategoryType): List<GroupedCategories> {
        return when (type) {
            CategoryType.Expense -> expense
            CategoryType.Income -> income
        }
    }

    fun replaceListByType(
        list: List<GroupedCategories>,
        type: CategoryType
    ): GroupedCategoriesByType {
        return when (type) {
            CategoryType.Expense -> this.copy(expense = list)
            CategoryType.Income -> this.copy(income = list)
        }
    }

    fun getFirstCategoryWithSubByType(type: CategoryType?): CategoryWithSub? {
        return type?.let { getByTypeOrAll(it).firstOrNull()?.getWithFirstSubcategory() }
    }

    fun getLastCategoryWithSubByType(type: CategoryType?): CategoryWithSub? {
        return type?.let { getByTypeOrAll(it).lastOrNull()?.getWithLastSubcategory() }
    }

    fun findById(id: Int, type: CategoryType? = null): GroupedCategories? {
        return getByTypeOrAll(type).firstOrNull { it.category.id == id }
    }

    fun appendNewCategory(category: Category): GroupedCategoriesByType {
        val list = getByType(category.type).toMutableList()
        list.add(GroupedCategories(
            category = category.copy(
                orderNum = (list.maxOfOrNull { it.category.orderNum } ?: 0) + 1
            )
        ))
        return replaceListByType(list, category.type)
    }

    fun replaceCategory(category: Category): GroupedCategoriesByType {
        val groupedCategoriesList = getByType(category.type).map {
            it.takeUnless { it.category.id == category.id } ?: it.copy(
                category = category,
                subcategoryList = it.changeSubcategoriesColorTo(category.color)
            )
        }

        return replaceListByType(
            list = groupedCategoriesList,
            type = category.type
        )
    }

    fun deleteCategoryById(category: Category): GroupedCategoriesByType {
        val newList = getByType(category.type).deleteItemAndMoveOrderNum(
            predicate = { it.category.id == category.id },
            transform = { it.copy(category = it.category.copy(orderNum = it.category.orderNum - 1)) }
        )
        return replaceListByType(newList, category.type)
    }

    fun fixParentCategoriesOrderNums(): GroupedCategoriesByType {
        return this.copy(
            expense = expense.fixParentOrderNums(),
            income = income.fixParentOrderNums()
        )
    }

}
