package com.ataglance.walletglance.model

import com.ataglance.walletglance.data.Category

enum class CategoryType {
    Expense, Income
}
enum class CategoryRank {
    Parent, Sub
}

data class ParentCategoriesLists(
    val expense: List<Category>,
    val income: List<Category>
)

data class SubcategoriesLists(
    val expense: List<List<Category>>,
    val income: List<List<Category>>
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
                getCategoryOrderNumById(parentCategoryId, parentCategoriesList)?.let { parentCategoryOrderNum ->
                    twoDimenList[parentCategoryOrderNum - 1].add(category)
                }
            }
        }

        return twoDimenList
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

}