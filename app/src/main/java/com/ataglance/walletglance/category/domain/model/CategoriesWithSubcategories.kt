package com.ataglance.walletglance.category.domain.model

import com.ataglance.walletglance.category.domain.utils.fixParentOrderNums
import com.ataglance.walletglance.category.domain.utils.toEditingCategoryWithSubcategoriesList
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionType
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionWithCategories
import com.ataglance.walletglance.core.utils.deleteItemAndMoveOrderNum
import com.ataglance.walletglance.record.domain.RecordStack
import com.ataglance.walletglance.record.domain.RecordStackItem
import com.ataglance.walletglance.record.domain.RecordType

data class CategoriesWithSubcategories(
    val expense: List<CategoryWithSubcategories> = emptyList(),
    val income: List<CategoryWithSubcategories> = emptyList()
) {

    fun concatenateAsCategoryList(): List<Category> {
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
                subcategoryList = it.changeSubcategoriesColorTo(category.colorWithName)
            )
        }

        return replaceListByType(
            list = categoryWithSubcategoriesList,
            type = category.type
        )
    }

    fun deleteCategoryById(category: Category): CategoriesWithSubcategories {
        val newList = getByType(category.type).deleteItemAndMoveOrderNum(
            { it.category.id == category.id },
            { it.copy(category = it.category.copy(orderNum = it.category.orderNum - 1)) }
        )
        return replaceListByType(newList, category.type)
    }

    fun fixParentCategoriesOrderNums(): CategoriesWithSubcategories {
        return this.copy(
            expense = expense.fixParentOrderNums(),
            income = income.fixParentOrderNums()
        )
    }

    fun getStatistics(recordStackList: List<RecordStack>): CategoryStatisticsLists {
        return CategoryStatisticsLists(
            expense = getCategoryStatisticsByType(
                recordStackList = recordStackList,
                type = RecordType.Expense,
            ),
            income = getCategoryStatisticsByType(
                recordStackList = recordStackList,
                type = RecordType.Income,
            )
        )
    }

    private fun getCategoryStatisticsByType(
        recordStackList: List<RecordStack>,
        type: RecordType,
    ): List<CategoryStatisticsElementUiState> {
        val categoryStatsMap = mutableMapOf<Int, CategoriesStatsMapItem>()
        val subcategoriesStatsMap = mutableMapOf<Int, MutableMap<Int, CategoriesStatsMapItem>>()

        recordStackList.forEach { recordStack ->
            if (recordStack.isOfType(type)) {
                recordStack.stack.forEach { stackUnit ->
                    increaseTotalAmountOrAddNewOneInCategoryStatsMap(
                        categoryStatsMap = categoryStatsMap,
                        stackUnit = stackUnit
                    )
                    increaseTotalAmountOrAddNewOneInSubcategoriesStatsMap(
                        subcategoriesStatsMap = subcategoriesStatsMap,
                        stackUnit = stackUnit
                    )
                }
            }
        }

        val accountCurrency = recordStackList.firstOrNull()?.account?.currency ?: ""
        val totalAmount = categoryStatsMap.values.sumOf { it.totalAmount }
        return categoryStatsMap.values.sortedByDescending { it.totalAmount }
            .map { statsMapItem ->
                statsMapItem.toCategoryStatisticsElementUiState(
                    accountCurrency = accountCurrency,
                    allCategoriesTotalAmount = totalAmount,
                    subcategoriesStatistics = subcategoriesStatsMap[statsMapItem.category.id]
                )
            }
    }

    private fun increaseTotalAmountOrAddNewOneInCategoryStatsMap(
        categoryStatsMap: MutableMap<Int, CategoriesStatsMapItem>,
        stackUnit: RecordStackItem
    ) {
        stackUnit.categoryWithSubcategory ?: return
        val category = stackUnit.categoryWithSubcategory.category

        if (categoryStatsMap.containsKey(category.id)) {
            categoryStatsMap[category.id]!!.totalAmount += stackUnit.amount
        } else {
            categoryStatsMap[category.id] = CategoriesStatsMapItem(
                category = category,
                totalAmount = stackUnit.amount
            )
        }
    }

    private fun increaseTotalAmountOrAddNewOneInSubcategoriesStatsMap(
        subcategoriesStatsMap: MutableMap<Int, MutableMap<Int, CategoriesStatsMapItem>>,
        stackUnit: RecordStackItem
    ) {
        stackUnit.categoryWithSubcategory?.subcategory ?: return
        val category = stackUnit.categoryWithSubcategory.category
        val subcategory = stackUnit.categoryWithSubcategory.subcategory

        if (!subcategoriesStatsMap.containsKey(category.id)) {
            subcategoriesStatsMap[category.id] = mutableMapOf(
                subcategory.id to CategoriesStatsMapItem(subcategory, stackUnit.amount)
            )
            return
        }

        if (subcategoriesStatsMap[category.id]!!.containsKey(subcategory.id)) {
            subcategoriesStatsMap[category.id]!![subcategory.id]!!
                .totalAmount += stackUnit.amount
        } else {
            subcategoriesStatsMap[category.id]!![subcategory.id] =
                CategoriesStatsMapItem(
                    category = subcategory,
                    totalAmount = stackUnit.amount
                )
        }
    }

    fun toEditingCategoriesWithSubcategories(
        collection: CategoryCollectionWithCategories?
    ): EditingCategoriesWithSubcategories {
        val expensesAndIncome = (collection?.categoryList ?: emptyList())
            .partition { it.isExpense() }
        return EditingCategoriesWithSubcategories(
            expense = if (collection?.type != CategoryCollectionType.Income) {
                expense.toEditingCategoryWithSubcategoriesList(expensesAndIncome.first)
            } else {
                emptyList()
            },
            income = if (collection?.type != CategoryCollectionType.Expense) {
                income.toEditingCategoryWithSubcategoriesList(expensesAndIncome.second)
            } else {
                emptyList()
            }
        )
    }

}
