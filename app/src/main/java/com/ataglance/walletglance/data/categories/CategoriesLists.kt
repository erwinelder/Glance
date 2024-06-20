package com.ataglance.walletglance.data.categories

import com.ataglance.walletglance.domain.entities.Category
import com.ataglance.walletglance.ui.theme.theme.AppTheme
import com.ataglance.walletglance.ui.utils.findById
import com.ataglance.walletglance.ui.utils.getSubcategoryByIdAndParentOrderNum
import com.ataglance.walletglance.ui.viewmodels.CategoriesStatsMapItem
import com.ataglance.walletglance.ui.viewmodels.CategoryStatisticsElementUiState
import com.ataglance.walletglance.ui.viewmodels.CategoryStatisticsLists
import com.ataglance.walletglance.data.app.Colors
import com.ataglance.walletglance.data.records.RecordStack
import com.ataglance.walletglance.data.records.RecordStackUnit
import com.ataglance.walletglance.data.records.RecordType

data class CategoriesLists(
    val parentCategories: ParentCategoriesLists = ParentCategoriesLists(),
    val subcategories: SubcategoriesLists = SubcategoriesLists()
) {

    fun getParCategoryListAndSubcategoryListsByType(
        type: RecordType?
    ): Pair<List<Category>, List<List<Category>>> {
        return if (type == RecordType.Expense) {
            parentCategories.expense to subcategories.expense
        } else {
            parentCategories.income to subcategories.income
        }
    }

    fun concatenateLists(): List<Category> {
        return parentCategories.expense + subcategories.expense.flatten() +
                parentCategories.income + subcategories.income.flatten()
    }

    fun getCategoryPairByIds(
        parentCategoryId: Int,
        subcategoryId: Int?,
        type: CategoryType
    ): Pair<Category, Category?>? {
        return parentCategories.getByType(type)
            .find { it.id == parentCategoryId }
            ?.let { parentCategory ->
                parentCategory to subcategoryId?.let {
                    subcategories.getByType(type)
                        .getSubcategoryByIdAndParentOrderNum(subcategoryId, parentCategory.orderNum)
                }
            }
    }

    fun getStatistics(
        recordStackList: List<RecordStack>,
        categoryIconNameToIconResMap: Map<String, Int>,
        categoryColorNameToColorMap: Map<String, Colors>,
        appTheme: AppTheme?,
        accountCurrency: String
    ): CategoryStatisticsLists {
        return CategoryStatisticsLists(
            expense = getCategoriesStatisticsByType(
                recordStackList = recordStackList,
                type = RecordType.Expense,
                categoryList = parentCategories.expense,
                subcategoriesLists = subcategories.expense,
                categoryIconNameToIconResMap = categoryIconNameToIconResMap,
                categoryColorNameToColorMap = categoryColorNameToColorMap,
                appTheme = appTheme,
                accountCurrency = accountCurrency
            ),
            income = getCategoriesStatisticsByType(
                recordStackList = recordStackList,
                type = RecordType.Income,
                categoryList = parentCategories.income,
                subcategoriesLists = subcategories.income,
                categoryIconNameToIconResMap = categoryIconNameToIconResMap,
                categoryColorNameToColorMap = categoryColorNameToColorMap,
                appTheme = appTheme,
                accountCurrency = accountCurrency
            )
        )
    }

    private fun getCategoriesStatisticsByType(
        recordStackList: List<RecordStack>,
        type: RecordType,
        categoryList: List<Category>,
        subcategoriesLists: List<List<Category>>,
        categoryIconNameToIconResMap: Map<String, Int>,
        categoryColorNameToColorMap: Map<String, Colors>,
        appTheme: AppTheme?,
        accountCurrency: String
    ): List<CategoryStatisticsElementUiState> {
        val categoryStatsMap = mutableMapOf<Int, CategoriesStatsMapItem>()
        val subcategoriesStatsMap = mutableMapOf<Int, MutableMap<Int, CategoriesStatsMapItem>>()

        recordStackList.forEach { recordStack ->
            if (recordStack.isOfType(type)) {
                recordStack.stack.forEach { stackUnit ->
                    increaseTotalAmountOrAddNewOneInCategoryStatsMap(
                        categoryStatsMap, categoryList, stackUnit
                    )
                    increaseTotalAmountOrAddNewOneInSubcategoriesStatsMap(
                        categoryStatsMap, subcategoriesStatsMap, subcategoriesLists, stackUnit
                    )
                }
            }
        }

        val totalAmount = categoryStatsMap.values.sumOf { it.totalAmount }
        return categoryStatsMap.values.sortedByDescending { it.totalAmount }
            .map { categoryStatsMapItem ->
                categoryStatsMapItem.toCategoryStatisticsElementUiState(
                    categoryIconNameToIconResMap = categoryIconNameToIconResMap,
                    categoryColorNameToColorMap = categoryColorNameToColorMap,
                    appTheme = appTheme,
                    accountCurrency = accountCurrency,
                    allCategoriesTotalAmount = totalAmount,
                    subcategoriesStatistics = subcategoriesStatsMap[categoryStatsMapItem.category.id]
                )
            }
    }

    private fun increaseTotalAmountOrAddNewOneInCategoryStatsMap(
        categoryStatsMap: MutableMap<Int, CategoriesStatsMapItem>,
        categoryList: List<Category>,
        stackUnit: RecordStackUnit
    ) {
        if (categoryStatsMap.containsKey(stackUnit.categoryId)) {
            categoryStatsMap[stackUnit.categoryId]!!.totalAmount += stackUnit.amount
        } else {
            categoryList.findById(stackUnit.categoryId)?.let { category ->
                categoryStatsMap[stackUnit.categoryId] = CategoriesStatsMapItem(
                    category = category,
                    totalAmount = stackUnit.amount
                )
            }
        }
    }

    private fun increaseTotalAmountOrAddNewOneInSubcategoriesStatsMap(
        categoryStatsMap: MutableMap<Int, CategoriesStatsMapItem>,
        subcategoriesStatsMap: MutableMap<Int, MutableMap<Int, CategoriesStatsMapItem>>,
        subcategoriesLists: List<List<Category>>,
        unit: RecordStackUnit
    ) {
        if (unit.subcategoryId == null) return

        if (!subcategoriesStatsMap.containsKey(unit.categoryId)) {
            categoryStatsMap[unit.categoryId]?.let { categoryStatsMapItem ->
                subcategoriesLists
                    .getSubcategoryByIdAndParentOrderNum(
                        subcategoryId = unit.subcategoryId,
                        parentCategoryOrderNum = categoryStatsMapItem.category.orderNum
                    )
                    ?.let { subcategory ->
                        subcategoriesStatsMap[unit.categoryId] = mutableMapOf(
                            unit.subcategoryId to CategoriesStatsMapItem(
                                category = subcategory,
                                totalAmount = unit.amount
                            )
                        )
                    }
            }
            return
        }

        if (subcategoriesStatsMap[unit.categoryId]!!.containsKey(unit.subcategoryId)) {
            subcategoriesStatsMap[unit.categoryId]!![unit.subcategoryId]!!
                .totalAmount += unit.amount
        } else {
            subcategoriesLists
                .getSubcategoryByIdAndParentOrderNum(
                    subcategoryId = unit.subcategoryId,
                    parentCategoryOrderNum = categoryStatsMap[unit.categoryId]!!.category.orderNum
                )
                ?.let { subcategory ->
                    subcategoriesStatsMap[unit.categoryId]!![unit.subcategoryId] =
                        CategoriesStatsMapItem(
                            category = subcategory,
                            totalAmount = unit.amount
                        )
                }
        }
    }

}
