package com.ataglance.walletglance.data.categories

import com.ataglance.walletglance.data.app.AppTheme
import com.ataglance.walletglance.data.records.RecordStack
import com.ataglance.walletglance.data.records.RecordStackUnit
import com.ataglance.walletglance.data.records.RecordType
import com.ataglance.walletglance.ui.utils.findById
import com.ataglance.walletglance.ui.utils.getSubcategoryByIdAndParentOrderNum

data class CategoriesLists(
    val parentCategories: ParentCategoriesLists = ParentCategoriesLists(),
    val subcategories: SubcategoriesLists = SubcategoriesLists()
) {

    fun concatenateLists(): List<Category> {
        return parentCategories.expense + subcategories.expense.flatten() +
                parentCategories.income + subcategories.income.flatten()
    }

    fun getParAndSubCategoryByIds(
        parentCategoryId: Int,
        subcategoryId: Int?,
        type: CategoryType
    ): Pair<Category, Category?>? {
        return parentCategories.getByType(type)
            .findById(parentCategoryId)
            ?.let { parentCategory ->
                parentCategory to subcategoryId?.let {
                    subcategories.getByType(type)
                        .getSubcategoryByIdAndParentOrderNum(subcategoryId, parentCategory.orderNum)
                }
            }
    }

    fun getStatistics(
        recordStackList: List<RecordStack>,
        appTheme: AppTheme?,
        accountCurrency: String
    ): CategoryStatisticsLists {
        return CategoryStatisticsLists(
            expense = getCategoryStatisticsByType(
                recordStackList = recordStackList,
                type = RecordType.Expense,
                appTheme = appTheme,
                accountCurrency = accountCurrency
            ),
            income = getCategoryStatisticsByType(
                recordStackList = recordStackList,
                type = RecordType.Income,
                appTheme = appTheme,
                accountCurrency = accountCurrency
            )
        )
    }

    private fun getCategoryStatisticsByType(
        recordStackList: List<RecordStack>,
        type: RecordType,
        appTheme: AppTheme?,
        accountCurrency: String
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

        val totalAmount = categoryStatsMap.values.sumOf { it.totalAmount }
        return categoryStatsMap.values.sortedByDescending { it.totalAmount }
            .map { categoryStatsMapItem ->
                categoryStatsMapItem.toCategoryStatisticsElementUiState(
                    appTheme = appTheme,
                    accountCurrency = accountCurrency,
                    allCategoriesTotalAmount = totalAmount,
                    subcategoriesStatistics = subcategoriesStatsMap[categoryStatsMapItem.category.id]
                )
            }
    }

    private fun increaseTotalAmountOrAddNewOneInCategoryStatsMap(
        categoryStatsMap: MutableMap<Int, CategoriesStatsMapItem>,
        stackUnit: RecordStackUnit
    ) {
        stackUnit.category ?: return

        if (categoryStatsMap.containsKey(stackUnit.category.id)) {
            categoryStatsMap[stackUnit.category.id]!!.totalAmount += stackUnit.amount
        } else {
            categoryStatsMap[stackUnit.category.id] = CategoriesStatsMapItem(
                category = stackUnit.category,
                totalAmount = stackUnit.amount
            )
        }
    }

    private fun increaseTotalAmountOrAddNewOneInSubcategoriesStatsMap(
        subcategoriesStatsMap: MutableMap<Int, MutableMap<Int, CategoriesStatsMapItem>>,
        stackUnit: RecordStackUnit
    ) {
        if (stackUnit.category == null || stackUnit.subcategory == null) return

        if (!subcategoriesStatsMap.containsKey(stackUnit.category.id)) {
            subcategoriesStatsMap[stackUnit.category.id] = mutableMapOf(
                stackUnit.subcategory.id to CategoriesStatsMapItem(
                    category = stackUnit.subcategory,
                    totalAmount = stackUnit.amount
                )
            )
            return
        }

        if (subcategoriesStatsMap[stackUnit.category.id]!!.containsKey(stackUnit.subcategory.id)) {
            subcategoriesStatsMap[stackUnit.category.id]!![stackUnit.subcategory.id]!!
                .totalAmount += stackUnit.amount
        } else {
            subcategoriesStatsMap[stackUnit.category.id]!![stackUnit.subcategory.id] =
                CategoriesStatsMapItem(
                    category = stackUnit.subcategory,
                    totalAmount = stackUnit.amount
                )
        }
    }

}
