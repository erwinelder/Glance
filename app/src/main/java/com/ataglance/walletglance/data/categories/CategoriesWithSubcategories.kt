package com.ataglance.walletglance.data.categories

import com.ataglance.walletglance.data.app.AppTheme
import com.ataglance.walletglance.data.categoryCollections.CategoryCollectionType
import com.ataglance.walletglance.data.categoryCollections.CategoryCollectionWithCategories
import com.ataglance.walletglance.data.records.RecordStack
import com.ataglance.walletglance.data.records.RecordStackUnit
import com.ataglance.walletglance.data.records.RecordType
import com.ataglance.walletglance.ui.utils.deleteItemAndMoveOrderNum
import com.ataglance.walletglance.ui.utils.toEditingCategoryWithSubcategoriesList

data class CategoriesWithSubcategories(
    val expense: List<CategoryWithSubcategories> = emptyList(),
    val income: List<CategoryWithSubcategories> = emptyList()
) {

    fun getByTypeOrAll(type: CategoryType?): List<CategoryWithSubcategories> {
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

    fun getLastCategoryWithSubcategoryByType(type: CategoryType?): CategoryWithSubcategory? {
        return type?.let { getByTypeOrAll(it).lastOrNull()?.getWithLastSubcategory() }
    }

    fun findById(id: Int, type: CategoryType? = null): CategoryWithSubcategories? {
        return getByTypeOrAll(type).firstOrNull { it.category.id == id }
    }

    fun concatenateAsCategoryList(): List<Category> {
        return (expense + income).flatMap { categoryWithSubcategories ->
            categoryWithSubcategories.asSingleList()
        }
    }

    fun appendNewCategoryByType(
        category: Category,
        type: CategoryType
    ): CategoriesWithSubcategories {
        return when (type) {
            CategoryType.Expense -> this.copy(
                expense = expense + listOf(CategoryWithSubcategories(category))
            )
            CategoryType.Income -> this.copy(
                income = income + listOf(CategoryWithSubcategories(category))
            )
        }
    }

    fun replaceCategory(category: Category, type: CategoryType): CategoriesWithSubcategories {
        val categoryWithSubcategoriesList = this.getByType(type).map {
            it.takeIf { it.category.id != category.id } ?: it.copy(
                category = category,
                subcategoryList = it.changeSubcategoriesColorTo(category.colorWithName)
            )
        }

        return replaceListByType(
            list = categoryWithSubcategoriesList,
            type = type
        )
    }

    fun deleteCategoryById(category: Category): CategoriesWithSubcategories {
        val newList = getByType(category.type).deleteItemAndMoveOrderNum(
            { it.category.id == category.id },
            { it.copy(category = it.category.copy(orderNum = it.category.orderNum - 1)) }
        )
        return replaceListByType(newList, category.type)
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

    fun getStatistics(
        recordStackList: List<RecordStack>,
        appTheme: AppTheme?
    ): CategoryStatisticsLists {
        return CategoryStatisticsLists(
            expense = getCategoryStatisticsByType(
                recordStackList = recordStackList,
                type = RecordType.Expense,
                appTheme = appTheme,
            ),
            income = getCategoryStatisticsByType(
                recordStackList = recordStackList,
                type = RecordType.Income,
                appTheme = appTheme,
            )
        )
    }

    private fun getCategoryStatisticsByType(
        recordStackList: List<RecordStack>,
        type: RecordType,
        appTheme: AppTheme?,
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
                    appTheme = appTheme,
                    accountCurrency = accountCurrency,
                    allCategoriesTotalAmount = totalAmount,
                    subcategoriesStatistics = subcategoriesStatsMap[statsMapItem.category.id]
                )
            }
    }

    private fun increaseTotalAmountOrAddNewOneInCategoryStatsMap(
        categoryStatsMap: MutableMap<Int, CategoriesStatsMapItem>,
        stackUnit: RecordStackUnit
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
        stackUnit: RecordStackUnit
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
