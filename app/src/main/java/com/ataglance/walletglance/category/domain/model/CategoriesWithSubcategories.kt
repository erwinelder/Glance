package com.ataglance.walletglance.category.domain.model

import com.ataglance.walletglance.category.domain.utils.fixParentOrderNums
import com.ataglance.walletglance.core.utils.deleteItemAndMoveOrderNum

data class CategoriesWithSubcategories(
    val expense: List<CategoryWithSubcategories> = emptyList(),
    val income: List<CategoryWithSubcategories> = emptyList()
) {

    fun asSingleList(): List<Category> {
        return (expense + income).flatMap { categoryWithSubcategories ->
            categoryWithSubcategories.asSingleList()
        }
    }

    private fun getByTypeOrAll(type: CategoryType?): List<CategoryWithSubcategories> {
        return when (type) {
            CategoryType.Expense -> expense
            CategoryType.Income -> income
            else -> expense + income
        }
    }

    fun getByType(type: CategoryType): List<CategoryWithSubcategories> {
        return when (type) {
            CategoryType.Expense -> expense
            CategoryType.Income -> income
        }
    }

    fun replaceListByType(
        list: List<CategoryWithSubcategories>,
        type: CategoryType
    ): CategoriesWithSubcategories {
        return when (type) {
            CategoryType.Expense -> this.copy(expense = list)
            CategoryType.Income -> this.copy(income = list)
        }
    }

    fun getFirstCategoryWithSubcategoryByType(type: CategoryType?): CategoryWithSubcategory? {
        return type?.let { getByTypeOrAll(it).firstOrNull()?.getWithFirstSubcategory() }
    }

    fun getLastCategoryWithSubcategoryByType(type: CategoryType?): CategoryWithSubcategory? {
        return type?.let { getByTypeOrAll(it).lastOrNull()?.getWithLastSubcategory() }
    }

    fun findById(id: Int, type: CategoryType? = null): CategoryWithSubcategories? {
        return getByTypeOrAll(type).firstOrNull { it.category.id == id }
    }

    fun appendNewCategory(category: Category): CategoriesWithSubcategories {
        val list = getByType(category.type).toMutableList()
        list.add(CategoryWithSubcategories(
            category = category.copy(
                orderNum = (list.maxOfOrNull { it.category.orderNum } ?: 0) + 1
            )
        ))
        return replaceListByType(list, category.type)
    }

    fun replaceCategory(category: Category): CategoriesWithSubcategories {
        val categoryWithSubcategoriesList = this.getByType(category.type).map {
            it.takeIf { it.category.id != category.id } ?: it.copy(
                category = category,
                subcategoryList = it.changeSubcategoriesColorTo(category.color)
            )
        }

        return replaceListByType(
            list = categoryWithSubcategoriesList,
            type = category.type
        )
    }

    fun deleteCategoryById(category: Category): CategoriesWithSubcategories {
        val newList = getByType(category.type).deleteItemAndMoveOrderNum(
            predicate = { it.category.id == category.id },
            transform = { it.copy(category = it.category.copy(orderNum = it.category.orderNum - 1)) }
        )
        return replaceListByType(newList, category.type)
    }

    fun fixParentCategoriesOrderNums(): CategoriesWithSubcategories {
        return this.copy(
            expense = expense.fixParentOrderNums(),
            income = income.fixParentOrderNums()
        )
    }

}
