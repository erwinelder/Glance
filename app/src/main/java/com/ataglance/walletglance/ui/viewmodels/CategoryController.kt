package com.ataglance.walletglance.ui.viewmodels

import com.ataglance.walletglance.domain.entities.Category
import com.ataglance.walletglance.ui.theme.theme.AppTheme

enum class CategoryType {
    Expense, Income
}
enum class CategoryRank {
    Parent, Sub
}

data class ParentCategoriesLists(
    val expense: List<Category> = emptyList(),
    val income: List<Category> = emptyList()
)

data class SubcategoriesLists(
    val expense: List<List<Category>> = emptyList(),
    val income: List<List<Category>> = emptyList()
)

class CategoryController {

    fun getCategoryOrderNumById(id: Int, list: List<Category>): Int? {
        list.forEach { category ->
            if (category.id == id) {
                return category.orderNum
            }
        }
        return null
    }

    fun getParCategoryFromList(id: Int, list: List<Category>): Category? {
        list.forEach { category ->
            if (category.id == id) {
                return category
            }
        }
        return null
    }

    fun getSubcategByParCategOrderNumFromLists(
        subcategoryId: Int,
        parentCategoryOrderNum: Int,
        lists: List<List<Category>>
    ): Category? {
        lists[parentCategoryOrderNum - 1].forEach { category ->
            if (category.id == subcategoryId) {
                return category
            }
        }
        return null
    }

    fun getCategoryAndIconRes(
        categoriesUiState: CategoriesUiState,
        categoryNameAndIconMap: Map<String, Int>,
        categoryId: Int,
        subcategoryId: Int?,
        recordType: RecordType?
    ): Pair<Category?, Int?>? {
        val parentCategoryList = if (recordType == RecordType.Expense) {
            categoriesUiState.parentCategories.expense
        } else {
            categoriesUiState.parentCategories.income
        }

        return if (subcategoryId != null) {
            CategoryController().getCategoryOrderNumById(categoryId, parentCategoryList)?.let {
                CategoryController().getSubcategByParCategOrderNumFromLists(
                    subcategoryId = subcategoryId,
                    parentCategoryOrderNum = it,
                    if (recordType == RecordType.Expense) {
                        categoriesUiState.subcategories.expense
                    } else {
                        categoriesUiState.subcategories.income
                    }
                )?.let { category ->
                    category to categoryNameAndIconMap[category.iconName]
                }
            }
        } else {
            CategoryController().getParCategoryFromList(categoryId, parentCategoryList)
                ?.let { category ->
                    category to categoryNameAndIconMap[category.iconName]
                }
        }
    }

    private fun breakListOnParentCategoriesLists(categoriesList: List<Category>): ParentCategoriesLists {
        val expenseCategoriesList = mutableListOf<Category>()
        val incomeCategoriesList = mutableListOf<Category>()

        categoriesList.forEach { category ->
            if (category.rank == 'c') {
                if (category.type == '-') {
                    expenseCategoriesList.add(category)
                } else if (category.type == '+') {
                    incomeCategoriesList.add(category)
                }
            }
        }
        expenseCategoriesList.sortBy { it.orderNum }
        incomeCategoriesList.sortBy { it.orderNum }

        return ParentCategoriesLists(expenseCategoriesList, incomeCategoriesList)
    }

    private fun transformToTwoDimenSubcategoriesList(
        oneDimenList: List<Category>,
        parentCategoriesList: List<Category>
    ): List<List<Category>> {
        val twoDimenList = mutableListOf<MutableList<Category>>()

        for (i in 0..<(parentCategoriesList.size)) {
            twoDimenList.add(mutableListOf())
        }

        oneDimenList.forEach { category ->
            category.parentCategoryId?.let { parentCategoryId ->
                getCategoryOrderNumById(parentCategoryId, parentCategoriesList)
                    ?.let { parentCategoryOrderNum ->
                        twoDimenList[parentCategoryOrderNum - 1].add(category)
                    }
            }
        }

        for (categoryList in twoDimenList) {
            if (!checkCategoriesOrderNums(categoryList)) {
                throw IllegalAccessException("Subcategories order numbers are not correct")
            }
        }

        return twoDimenList
    }

    private fun checkCategoriesOrderNums(categoryList: List<Category>): Boolean {
        categoryList.sortedBy { it.orderNum }.forEachIndexed { index, category ->
            if (category.orderNum != index + 1) {
                return false
            }
        }
        return true
    }

    private fun breakListOnSubcategoriesLists(
        allCategoriesList: List<Category>,
        parentCategoriesLists: ParentCategoriesLists
    ): SubcategoriesLists {
        val expenseSubcategoriesList = mutableListOf<Category>()
        val incomeSubcategoriesList = mutableListOf<Category>()

        allCategoriesList.forEach { category ->
            if (category.rank == 's') {
                when (category.type) {
                    '-' -> expenseSubcategoriesList.add(category)
                    '+' -> incomeSubcategoriesList.add(category)
                }
            }
        }
        expenseSubcategoriesList.sortBy { it.orderNum }
        incomeSubcategoriesList.sortBy { it.orderNum }

        return SubcategoriesLists(
            transformToTwoDimenSubcategoriesList(expenseSubcategoriesList, parentCategoriesLists.expense),
            transformToTwoDimenSubcategoriesList(incomeSubcategoriesList, parentCategoriesLists.income)
        )
    }

    fun breakCategoriesOnDifferentLists(categoryList: List<Category>): CategoriesUiState {
        val parentCategoriesLists = breakListOnParentCategoriesLists(categoryList)
        val subcategoriesLists = breakListOnSubcategoriesLists(categoryList, parentCategoriesLists)

        return CategoriesUiState(parentCategoriesLists, subcategoriesLists)
    }

    fun concatenateCategoryLists(parentCategoryLists: ParentCategoriesLists, subcategoryLists: SubcategoriesLists): List<Category> {
        val finalList = parentCategoryLists.expense.toMutableList()

        subcategoryLists.expense.forEach { categoryList ->
            categoryList.forEach { category ->
                finalList.add(category)
            }
        }

        parentCategoryLists.income.forEach { category ->
            finalList.add(category)
        }

        subcategoryLists.income.forEach { categoryList ->
            categoryList.forEach { category ->
                finalList.add(category)
            }
        }

        return finalList
    }

    fun getCategoriesStatistics(
        recordStackList: List<RecordStack>,
        parentCategoriesLists: ParentCategoriesLists,
        subcategoriesLists: SubcategoriesLists,
        categoryIconNameToIconResMap: Map<String, Int>,
        categoryColorNameToColorMap: Map<String, Colors>,
        appTheme: AppTheme?,
        accountCurrency: String
    ): CategoryStatisticsLists {
        return CategoryStatisticsLists(
            expense = getCategoriesStatisticsByType(
                recordStackList = recordStackList,
                type = RecordType.Expense,
                categoryList = parentCategoriesLists.expense,
                subcategoriesLists = subcategoriesLists.expense,
                categoryIconNameToIconResMap = categoryIconNameToIconResMap,
                categoryColorNameToColorMap = categoryColorNameToColorMap,
                appTheme = appTheme,
                accountCurrency = accountCurrency
            ),
            income = getCategoriesStatisticsByType(
                recordStackList = recordStackList,
                type = RecordType.Income,
                categoryList = parentCategoriesLists.income,
                subcategoriesLists = subcategoriesLists.income,
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
            if (
                recordStack.isExpense() && type == RecordType.Expense ||
                recordStack.isIncome() && type == RecordType.Income
            ) {
                recordStack.stack.forEach { stackUnit ->
                    if (categoryStatsMap.containsKey(stackUnit.categoryId)) {
                        categoryStatsMap[stackUnit.categoryId]!!.totalAmount += stackUnit.amount
                    } else {
                        categoryList.find { it.id == stackUnit.categoryId }?.let { category ->
                            categoryStatsMap[stackUnit.categoryId] = CategoriesStatsMapItem(
                                category = category,
                                totalAmount = stackUnit.amount
                            )
                        }
                    }
                    if (stackUnit.subcategoryId != null) {
                        if (subcategoriesStatsMap.containsKey(stackUnit.categoryId)) {
                            if (subcategoriesStatsMap[stackUnit.categoryId]!!.containsKey(stackUnit.subcategoryId)) {
                                subcategoriesStatsMap[stackUnit.categoryId]!![stackUnit.subcategoryId]!!.totalAmount += stackUnit.amount
                            } else {
                                subcategoriesLists.getOrNull(
                                    categoryStatsMap[stackUnit.categoryId]!!.category.orderNum - 1
                                )
                                    ?.find { it.id == stackUnit.subcategoryId }
                                    ?.let { subcategory ->
                                        subcategoriesStatsMap[stackUnit.categoryId]!![stackUnit.subcategoryId] =
                                            CategoriesStatsMapItem(
                                                category = subcategory,
                                                totalAmount = stackUnit.amount
                                            )
                                    }
                            }
                        } else {
                            categoryStatsMap[stackUnit.categoryId]?.let { categoryStatsMapItem ->
                                subcategoriesLists.getOrNull(
                                    categoryStatsMapItem.category.orderNum - 1
                                )
                                    ?.find { it.id == stackUnit.subcategoryId }
                                    ?.let { subcategory ->
                                        subcategoriesStatsMap[stackUnit.categoryId] = mutableMapOf(
                                            stackUnit.subcategoryId to CategoriesStatsMapItem(
                                                category = subcategory,
                                                totalAmount = stackUnit.amount
                                            )
                                        )
                                    }
                            }
                        }
                    }
                }
            }
        }

        val totalAmount = categoryStatsMap.values.sumOf { it.totalAmount }

        return categoryStatsMap.values
            .sortedByDescending { it.totalAmount }
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

    fun fixCategoriesOrderNums(categoryList: List<Category>): List<Category> {
        val fixedCategoryList = mutableListOf<Category>()

        val categoryListsExpenseAndIncome = categoryList.partition { it.isExpense() }

        val expenseCategoryListsParAndSub =
            categoryListsExpenseAndIncome.first.partition { it.isParentCategory() }
        val incomeCategoryListsParAndSub =
            categoryListsExpenseAndIncome.second.partition { it.isParentCategory() }

        expenseCategoryListsParAndSub.first
            .sortedBy { it.orderNum }
            .forEachIndexed { index, category ->
                fixedCategoryList.add(category.copy(orderNum = index + 1))
            }

        incomeCategoryListsParAndSub.first
            .sortedBy { it.orderNum }
            .forEachIndexed { index, category ->
                fixedCategoryList.add(category.copy(orderNum = index + 1))
            }

        expenseCategoryListsParAndSub.second.sortedBy { it.parentCategoryId }
            .forEach { subcategory ->
                if (
                    fixedCategoryList.lastOrNull()?.isParentCategory() == true ||
                    subcategory.parentCategoryId != fixedCategoryList.lastOrNull()?.parentCategoryId
                ) {
                    fixedCategoryList.add(subcategory.copy(orderNum = 1))
                } else {
                    fixedCategoryList.lastOrNull()?.let {
                        fixedCategoryList.add(subcategory.copy(orderNum = it.orderNum + 1))
                    }
                }
            }

        incomeCategoryListsParAndSub.second.sortedBy { it.parentCategoryId }
            .forEach { subcategory ->
                if (
                    fixedCategoryList.lastOrNull()?.isParentCategory() == true &&
                    subcategory.parentCategoryId != fixedCategoryList.lastOrNull()?.parentCategoryId
                ) {
                    fixedCategoryList.add(subcategory.copy(orderNum = 1))
                } else {
                    fixedCategoryList.lastOrNull()?.let {
                        fixedCategoryList.add(subcategory.copy(orderNum = it.orderNum + 1))
                    }
                }
            }

        return fixedCategoryList
    }

}