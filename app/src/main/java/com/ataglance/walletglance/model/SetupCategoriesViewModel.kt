package com.ataglance.walletglance.model

import android.content.Context
import androidx.lifecycle.ViewModel
import com.ataglance.walletglance.R
import com.ataglance.walletglance.data.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SetupCategoriesViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SetupCategoriesUiState())
    val uiState = _uiState.asStateFlow()

    private val _editCategoryUiState = MutableStateFlow(EditCategoryUiState())
    val editCategoryUiState = _editCategoryUiState.asStateFlow()

    private fun getCategoryFromListByOrderNum(list: List<Category>, orderNum: Int): Category? {
        list.forEach { category ->
            if (category.orderNum == orderNum) {
                return category
            }
        }
        return null
    }

    private fun getNewCategoryId(): Int {
        var maxFoundedId = 0

        uiState.value.expenseParentCategoryList.forEach { category ->
            if (category.id > maxFoundedId) {
                maxFoundedId = category.id
            }
        }
        uiState.value.incomeParentCategoryList.forEach { category ->
            if (category.id > maxFoundedId) {
                maxFoundedId = category.id
            }
        }
        uiState.value.expenseSubcategoryLists.forEach { categoryList ->
            categoryList.forEach { category ->
                if (category.id > maxFoundedId) {
                    maxFoundedId = category.id
                }
            }
        }
        uiState.value.incomeSubcategoryLists.forEach { categoryList ->
            categoryList.forEach { category ->
                if (category.id > maxFoundedId) {
                    maxFoundedId = category.id
                }
            }
        }

        return maxFoundedId + 1
    }

    private fun getNewCategory(
        type: CategoryType,
        rank: CategoryRank,
        listSize: Int,
        parentCategoryId: Int?,
        name: String,
        colorName: String = CategoryColors.GrayDefault(null).color.name
    ): Category {
        return Category(
            id = getNewCategoryId(),
            type = when (type) {
                CategoryType.Expense -> '-'
                CategoryType.Income -> '+'
            },
            rank = if (rank == CategoryRank.Parent) 'c' else 's',
            orderNum = listSize + 1,
            parentCategoryId = parentCategoryId,
            name = name,
            iconName = "other",
            colorName = colorName
        )
    }

    private fun getCurrentParentCategoryListByType(): List<Category> {
        return if (uiState.value.categoryTypeToShow == CategoryType.Expense) {
            uiState.value.expenseParentCategoryList
        } else {
            uiState.value.incomeParentCategoryList
        }
    }

    private fun changeParentCategoryIdToParentCategory(): List<Category> {
        val orderNum = uiState.value.parentCategoryOrderNum
        val categoryList = if (uiState.value.categoryTypeToShow == CategoryType.Expense) {
            uiState.value.expenseParentCategoryList.toMutableList()
        } else {
            uiState.value.incomeParentCategoryList.toMutableList()
        }

        categoryList[orderNum - 1].let {
            categoryList[orderNum - 1] = it.copy(parentCategoryId = it.id)
        }
        return categoryList
    }

    private fun getSubcategoryListByOrderNum(parentCategoryOrderNum: Int): List<Category> {
        return if (uiState.value.categoryTypeToShow == CategoryType.Expense) {
            uiState.value.expenseSubcategoryLists[parentCategoryOrderNum - 1]
        } else {
            uiState.value.incomeSubcategoryLists[parentCategoryOrderNum - 1]
        }
    }

    private fun getDefaultCategoriesPackage(context: Context): SetupCategoriesUiState {
        return SetupCategoriesUiState(
                expenseParentCategoryList = listOf(
                    Category(
                        id = 1, type = '-', rank = 'c', orderNum = 1, parentCategoryId = 1,
                        name = context.getString(R.string.food_and_drinks), iconName = "food_and_drinks",
                        colorName = CategoryColors.Olive(null).color.name
                    ),
                    Category(
                        id = 2, type = '-', rank = 'c', orderNum = 2, parentCategoryId = 2,
                        name = context.getString(R.string.housing), iconName = "housing",
                        colorName = CategoryColors.Camel(null).color.name
                    ),
                    Category(
                        id = 3, type = '-', rank = 'c', orderNum = 3, parentCategoryId = 3,
                        name = context.getString(R.string.shopping), iconName = "shopping",
                        colorName = CategoryColors.Pink(null).color.name
                    ),
                    Category(
                        id = 4, type = '-', rank = 'c', orderNum = 4, parentCategoryId = 4,
                        name = context.getString(R.string.transport), iconName = "transport",
                        colorName = CategoryColors.Green(null).color.name
                    ),
                    Category(
                        id = 5, type = '-', rank = 'c', orderNum = 5, parentCategoryId = 5,
                        name = context.getString(R.string.vehicle), iconName = "vehicle",
                        colorName = CategoryColors.Red(null).color.name
                    ),
                    Category(
                        id = 6, type = '-', rank = 'c', orderNum = 6, parentCategoryId = 6,
                        name = context.getString(R.string.digital_life), iconName = "digital_life",
                        colorName = CategoryColors.LightBlue(null).color.name
                    ),
                    Category(
                        id = 7, type = '-', rank = 'c', orderNum = 7, parentCategoryId = 7,
                        name = context.getString(R.string.medicine), iconName = "medicine",
                        colorName = CategoryColors.Lavender(null).color.name
                    ),
                    Category(
                        id = 8, type = '-', rank = 'c', orderNum = 8, parentCategoryId = 8,
                        name = context.getString(R.string.education), iconName = "education",
                        colorName = CategoryColors.Blue(null).color.name
                    ),
                    Category(
                        id = 9, type = '-', rank = 'c', orderNum = 9, parentCategoryId = 9,
                        name = context.getString(R.string.travels), iconName = "travels",
                        colorName = CategoryColors.Aquamarine(null).color.name
                    ),
                    Category(
                        id = 10, type = '-', rank = 'c', orderNum = 10, parentCategoryId = 10,
                        name = context.getString(R.string.entertainment), iconName = "entertainment",
                        colorName = CategoryColors.Orange(null).color.name
                    ),
                    Category(
                        id = 11, type = '-', rank = 'c', orderNum = 11, parentCategoryId = 11,
                        name = context.getString(R.string.investments), iconName = "investments",
                        colorName = CategoryColors.Yellow(null).color.name
                    ),
                    Category(
                        id = 12, type = '-', rank = 'c', orderNum = 12, parentCategoryId = 12,
                        name = context.getString(R.string.other_category), iconName = "other",
                        colorName = CategoryColors.GrayDefault(null).color.name
                    )
                ),
                expenseSubcategoryLists = listOf(
                    listOf(
                        Category(
                            id = 13, type = '-', rank = 's', orderNum = 1, parentCategoryId = 1,
                            name = context.getString(R.string.groceries), iconName = "food_and_drinks",
                            colorName = CategoryColors.Olive(null).color.name
                        ),
                        Category(
                            id = 14, type = '-', rank = 's', orderNum = 2, parentCategoryId = 1,
                            name = context.getString(R.string.restaurant), iconName = "restaurant",
                            colorName = CategoryColors.Olive(null).color.name
                        )
                    ),
                    listOf(
                        Category(
                            id = 15, type = '-', rank = 's', orderNum = 1, parentCategoryId = 2,
                            name = context.getString(R.string.utilities), iconName = "housing",
                            colorName = CategoryColors.Camel(null).color.name
                        ),
                        Category(
                            id = 16, type = '-', rank = 's', orderNum = 2, parentCategoryId = 2,
                            name = context.getString(R.string.services), iconName = "housing",
                            colorName = CategoryColors.Camel(null).color.name
                        ),
                        Category(
                            id = 17, type = '-', rank = 's', orderNum = 3, parentCategoryId = 2,
                            name = context.getString(R.string.rent), iconName = "housing",
                            colorName = CategoryColors.Camel(null).color.name
                        ),
                        Category(
                            id = 18, type = '-', rank = 's', orderNum = 4, parentCategoryId = 2,
                            name = context.getString(R.string.mortgage), iconName = "housing",
                            colorName = CategoryColors.Camel(null).color.name
                        ),
                        Category(
                            id = 19, type = '-', rank = 's', orderNum = 5, parentCategoryId = 2,
                            name = context.getString(R.string.maintenance), iconName = "housing",
                            colorName = CategoryColors.Camel(null).color.name
                        ),
                        Category(
                            id = 20, type = '-', rank = 's', orderNum = 6, parentCategoryId = 2,
                            name = context.getString(R.string.purchase), iconName = "housing_purchase",
                            colorName = CategoryColors.Camel(null).color.name
                        )
                    ),
                    listOf(
                        Category(
                            id = 21, type = '-', rank = 's', orderNum = 1, parentCategoryId = 3,
                            name = context.getString(R.string.clothes_and_shoes), iconName = "shopping",
                            colorName = CategoryColors.Pink(null).color.name
                        ),
                        Category(
                            id = 22, type = '-', rank = 's', orderNum = 2, parentCategoryId = 3,
                            name = context.getString(R.string.electronics_accessories), iconName = "shopping",
                            colorName = CategoryColors.Pink(null).color.name
                        ),
                        Category(
                            id = 23, type = '-', rank = 's', orderNum = 3, parentCategoryId = 3,
                            name = context.getString(R.string.cleaning_and_laundry), iconName = "shopping",
                            colorName = CategoryColors.Pink(null).color.name
                        ),
                        Category(
                            id = 24, type = '-', rank = 's', orderNum = 4, parentCategoryId = 3,
                            name = context.getString(R.string.health_and_beauty), iconName = "shopping",
                            colorName = CategoryColors.Pink(null).color.name
                        ),
                        Category(
                            id = 25, type = '-', rank = 's', orderNum = 5, parentCategoryId = 3,
                            name = context.getString(R.string.home_and_garden), iconName = "shopping",
                            colorName = CategoryColors.Pink(null).color.name
                        ),
                        Category(
                            id = 26, type = '-', rank = 's', orderNum = 6, parentCategoryId = 3,
                            name = context.getString(R.string.books), iconName = "shopping",
                            colorName = CategoryColors.Pink(null).color.name
                        ),
                        Category(
                            id = 27, type = '-', rank = 's', orderNum = 7, parentCategoryId = 3,
                            name = context.getString(R.string.joy_and_gifts), iconName = "shopping",
                            colorName = CategoryColors.Pink(null).color.name
                        ),
                        Category(
                            id = 28, type = '-', rank = 's', orderNum = 8, parentCategoryId = 3,
                            name = context.getString(R.string.kids), iconName = "shopping",
                            colorName = CategoryColors.Pink(null).color.name
                        ),
                        Category(
                            id = 29, type = '-', rank = 's', orderNum = 9, parentCategoryId = 3,
                            name = context.getString(R.string.pets_and_animals), iconName = "shopping",
                            colorName = CategoryColors.Pink(null).color.name
                        )
                    ),
                    listOf(
                        Category(
                            id = 30, type = '-', rank = 's', orderNum = 1, parentCategoryId = 4,
                            name = context.getString(R.string.public_transport), iconName = "transport",
                            colorName = CategoryColors.Green(null).color.name
                        ),
                        Category(
                            id = 31, type = '-', rank = 's', orderNum = 2, parentCategoryId = 4,
                            name = context.getString(R.string.taxi), iconName = "transport",
                            colorName = CategoryColors.Green(null).color.name
                        ),
                        Category(
                            id = 32, type = '-', rank = 's', orderNum = 3, parentCategoryId = 4,
                            name = context.getString(R.string.business_trips), iconName = "transport",
                            colorName = CategoryColors.Green(null).color.name
                        )
                    ),
                    listOf(
                        Category(
                            id = 33, type = '-', rank = 's', orderNum = 1, parentCategoryId = 5,
                            name = context.getString(R.string.parking), iconName = "vehicle",
                            colorName = CategoryColors.Red(null).color.name
                        ),
                        Category(
                            id = 34, type = '-', rank = 's', orderNum = 2, parentCategoryId = 5,
                            name = context.getString(R.string.fuel), iconName = "vehicle",
                            colorName = CategoryColors.Red(null).color.name
                        ),
                        Category(
                            id = 35, type = '-', rank = 's', orderNum = 3, parentCategoryId = 5,
                            name = context.getString(R.string.leasing), iconName = "vehicle",
                            colorName = CategoryColors.Red(null).color.name
                        ),
                        Category(
                            id = 36, type = '-', rank = 's', orderNum = 4, parentCategoryId = 5,
                            name = context.getString(R.string.rent), iconName = "vehicle",
                            colorName = CategoryColors.Red(null).color.name
                        ),
                        Category(
                            id = 37, type = '-', rank = 's', orderNum = 5, parentCategoryId = 5,
                            name = context.getString(R.string.maintenance), iconName = "vehicle",
                            colorName = CategoryColors.Red(null).color.name
                        )
                    ),
                    listOf(
                        Category(
                            id = 38, type = '-', rank = 's', orderNum = 1, parentCategoryId = 6,
                            name = context.getString(R.string.internet_communication), iconName = "digital_life",
                            colorName = CategoryColors.LightBlue(null).color.name
                        ),
                        Category(
                            id = 39, type = '-', rank = 's', orderNum = 2, parentCategoryId = 6,
                            name = context.getString(R.string.professional_subs), iconName = "digital_life",
                            colorName = CategoryColors.LightBlue(null).color.name
                        ),
                        Category(
                            id = 40, type = '-', rank = 's', orderNum = 3, parentCategoryId = 6,
                            name = context.getString(R.string.entertainment_subs), iconName = "digital_life",
                            colorName = CategoryColors.LightBlue(null).color.name
                        ),
                        Category(
                            id = 41, type = '-', rank = 's', orderNum = 4, parentCategoryId = 6,
                            name = context.getString(R.string.games), iconName = "digital_life",
                            colorName = CategoryColors.LightBlue(null).color.name
                        )
                    ),
                    listOf(
                        Category(
                            id = 42, type = '-', rank = 's', orderNum = 1, parentCategoryId = 7,
                            name = context.getString(R.string.drugs_and_vitamins), iconName = "medicine",
                            colorName = CategoryColors.Lavender(null).color.name
                        ),
                        Category(
                            id = 43, type = '-', rank = 's', orderNum = 2, parentCategoryId = 7,
                            name = context.getString(R.string.insurance), iconName = "medicine",
                            colorName = CategoryColors.Lavender(null).color.name
                        ),
                        Category(
                            id = 44, type = '-', rank = 's', orderNum = 3, parentCategoryId = 7,
                            name = context.getString(R.string.doctor), iconName = "medicine",
                            colorName = CategoryColors.Lavender(null).color.name
                        ),
                        Category(
                            id = 45, type = '-', rank = 's', orderNum = 4, parentCategoryId = 7,
                            name = context.getString(R.string.vaccination), iconName = "medicine",
                            colorName = CategoryColors.Lavender(null).color.name
                        )
                    ),
                    listOf(
                        Category(
                            id = 46, type = '-', rank = 's', orderNum = 1, parentCategoryId = 8,
                            name = context.getString(R.string.school_and_university), iconName = "education",
                            colorName = CategoryColors.Blue(null).color.name
                        ),
                        Category(
                            id = 47, type = '-', rank = 's', orderNum = 2, parentCategoryId = 8,
                            name = context.getString(R.string.courses), iconName = "education",
                            colorName = CategoryColors.Blue(null).color.name
                        ),
                        Category(
                            id = 48, type = '-', rank = 's', orderNum = 3, parentCategoryId = 8,
                            name = context.getString(R.string.studding_materials), iconName = "education",
                            colorName = CategoryColors.Blue(null).color.name
                        )
                    ),
                    listOf(
                        Category(
                            id = 49, type = '-', rank = 's', orderNum = 1, parentCategoryId = 9,
                            name = context.getString(R.string.tickets), iconName = "travels",
                            colorName = CategoryColors.Aquamarine(null).color.name
                        ),
                        Category(
                            id = 50, type = '-', rank = 's', orderNum = 2, parentCategoryId = 9,
                            name = context.getString(R.string.accommodation), iconName = "travels",
                            colorName = CategoryColors.Aquamarine(null).color.name
                        ),
                        Category(
                            id = 51, type = '-', rank = 's', orderNum = 3, parentCategoryId = 9,
                            name = context.getString(R.string.food_and_drinks), iconName = "travels",
                            colorName = CategoryColors.Aquamarine(null).color.name
                        ),
                        Category(
                            id = 52, type = '-', rank = 's', orderNum = 4, parentCategoryId = 9,
                            name = context.getString(R.string.transport), iconName = "travels",
                            colorName = CategoryColors.Aquamarine(null).color.name
                        ),
                        Category(
                            id = 53, type = '-', rank = 's', orderNum = 5, parentCategoryId = 9,
                            name = context.getString(R.string.shopping), iconName = "travels",
                            colorName = CategoryColors.Aquamarine(null).color.name
                        ),
                        Category(
                            id = 54, type = '-', rank = 's', orderNum = 6, parentCategoryId = 9,
                            name = context.getString(R.string.entertainment), iconName = "travels",
                            colorName = CategoryColors.Aquamarine(null).color.name
                        ),
                        Category(
                            id = 55, type = '-', rank = 's', orderNum = 7, parentCategoryId = 9,
                            name = context.getString(R.string.other_category), iconName = "travels",
                            colorName = CategoryColors.Aquamarine(null).color.name
                        )
                    ),
                    listOf(
                        Category(
                            id = 56, type = '-', rank = 's', orderNum = 1, parentCategoryId = 10,
                            name = context.getString(R.string.hobbies), iconName = "entertainment",
                            colorName = CategoryColors.Orange(null).color.name
                        ),
                        Category(
                            id = 57, type = '-', rank = 's', orderNum = 2, parentCategoryId = 10,
                            name = context.getString(R.string.cinema_theater_concerts), iconName = "entertainment",
                            colorName = CategoryColors.Orange(null).color.name
                        ),
                        Category(
                            id = 58, type = '-', rank = 's', orderNum = 3, parentCategoryId = 10,
                            name = context.getString(R.string.sport), iconName = "entertainment",
                            colorName = CategoryColors.Orange(null).color.name
                        ),
                        Category(
                            id = 59, type = '-', rank = 's', orderNum = 4, parentCategoryId = 10,
                            name = context.getString(R.string.events_hanging_out), iconName = "entertainment",
                            colorName = CategoryColors.Orange(null).color.name
                        ),
                        Category(
                            id = 60, type = '-', rank = 's', orderNum = 5, parentCategoryId = 10,
                            name = context.getString(R.string.alcohol_and_smoking), iconName = "entertainment",
                            colorName = CategoryColors.Orange(null).color.name
                        ),
                        Category(
                            id = 61, type = '-', rank = 's', orderNum = 6, parentCategoryId = 10,
                            name = context.getString(R.string.gifts_and_charity), iconName = "entertainment",
                            colorName = CategoryColors.Orange(null).color.name
                        )
                    ),
                    listOf(
                        Category(
                            id = 62, type = '-', rank = 's', orderNum = 1, parentCategoryId = 11,
                            name = context.getString(R.string.shares), iconName = "investments",
                            colorName = CategoryColors.Yellow(null).color.name
                        ),
                        Category(
                            id = 63, type = '-', rank = 's', orderNum = 2, parentCategoryId = 11,
                            name = context.getString(R.string.cryptocurrencies), iconName = "investments",
                            colorName = CategoryColors.Yellow(null).color.name
                        ),
                        Category(
                            id = 64, type = '-', rank = 's', orderNum = 3, parentCategoryId = 11,
                            name = context.getString(R.string.realty), iconName = "investments",
                            colorName = CategoryColors.Yellow(null).color.name
                        ),
                        Category(
                            id = 65, type = '-', rank = 's', orderNum = 4, parentCategoryId = 11,
                            name = context.getString(R.string.savings), iconName = "investments",
                            colorName = CategoryColors.Yellow(null).color.name
                        )
                    ),
                    listOf(
                        Category(
                            id = 66, type = '-', rank = 's', orderNum = 1, parentCategoryId = 12,
                            name = context.getString(R.string.transfers), iconName = "transfers",
                            colorName = CategoryColors.GrayDefault(null).color.name
                        ),
                        Category(
                            id = 67, type = '-', rank = 's', orderNum = 2, parentCategoryId = 12,
                            name = context.getString(R.string.missing), iconName = "missing",
                            colorName = CategoryColors.GrayDefault(null).color.name
                        ),
                        Category(
                            id = 68, type = '-', rank = 's', orderNum = 3, parentCategoryId = 12,
                            name = context.getString(R.string.public_toilets), iconName = "other",
                            colorName = CategoryColors.GrayDefault(null).color.name
                        ),
                        Category(
                            id = 69, type = '-', rank = 's', orderNum = 4, parentCategoryId = 12,
                            name = context.getString(R.string.other_category), iconName = "other",
                            colorName = CategoryColors.GrayDefault(null).color.name
                        )
                    )
                ),
                incomeParentCategoryList = listOf(
                    Category(
                        id = 70, type = '+', rank = 'c', orderNum = 1, parentCategoryId = null,
                        name = context.getString(R.string.salary), iconName = "salary",
                            colorName = CategoryColors.Green(null).color.name
                    ),
                    Category(
                        id = 71, type = '+', rank = 'c', orderNum = 2, parentCategoryId = null,
                        name = context.getString(R.string.scholarship), iconName = "scholarship",
                            colorName = CategoryColors.Blue(null).color.name
                    ),
                    Category(
                        id = 72, type = '+', rank = 'c', orderNum = 3, parentCategoryId = null,
                        name = context.getString(R.string.sale), iconName = "sales",
                            colorName = CategoryColors.Orange(null).color.name
                    ),
                    Category(
                        id = 73, type = '+', rank = 'c', orderNum = 4, parentCategoryId = null,
                        name = context.getString(R.string.rent), iconName = "housing",
                            colorName = CategoryColors.Camel(null).color.name
                    ),
                    Category(
                        id = 74, type = '+', rank = 'c', orderNum = 5, parentCategoryId = null,
                        name = context.getString(R.string.refunds), iconName = "refunds",
                            colorName = CategoryColors.LightBlue(null).color.name
                    ),
                    Category(
                        id = 75, type = '+', rank = 'c', orderNum = 6, parentCategoryId = null,
                        name = context.getString(R.string.investments), iconName = "investments",
                            colorName = CategoryColors.Yellow(null).color.name
                    ),
                    Category(
                        id = 76, type = '+', rank = 'c', orderNum = 7, parentCategoryId = null,
                        name = context.getString(R.string.gifts), iconName = "gifts",
                            colorName = CategoryColors.Lavender(null).color.name
                    ),
                    Category(
                        id = 77, type = '+', rank = 'c', orderNum = 8, parentCategoryId = null,
                        name = context.getString(R.string.transfers), iconName = "transfers",
                            colorName = CategoryColors.GrayDefault(null).color.name
                    )
                ),
                incomeSubcategoryLists = listOf(
                    emptyList(),
                    emptyList(),
                    emptyList(),
                    emptyList(),
                    emptyList(),
                    emptyList(),
                    emptyList(),
                    emptyList()
                )
            )

    }

    private fun getSwappedSubcategoryLists(firstParentOrderNum: Int, secondParentOrderNum: Int): List<List<Category>> {
        val subcategoryLists = if (uiState.value.categoryTypeToShow == CategoryType.Expense) {
            uiState.value.expenseSubcategoryLists.toMutableList()
        } else {
            uiState.value.incomeSubcategoryLists.toMutableList()
        }
        val firstList = subcategoryLists[firstParentOrderNum - 1]
        val secondList = subcategoryLists[secondParentOrderNum - 1]
        subcategoryLists[secondParentOrderNum - 1] = firstList
        subcategoryLists[firstParentOrderNum - 1] = secondList

        return subcategoryLists
    }

    private fun saveEditedCategoryToList(categoryToSave: Category): List<Category> {
        val isParentCategory = uiState.value.subcategoryList.isEmpty()
        val isExpenseType = uiState.value.categoryTypeToShow == CategoryType.Expense

        val list = if (isParentCategory) {
            if (isExpenseType) uiState.value.expenseParentCategoryList
            else uiState.value.incomeParentCategoryList
        } else {
            uiState.value.subcategoryList
        }
        val newList = mutableListOf<Category>()

        list.forEach { category ->
            if (category.orderNum != categoryToSave.orderNum) {
                newList.add(category)
            } else {
                newList.add(categoryToSave)
            }
        }

        return newList
    }

    private fun deleteSubcategoriesOfParentCategory(parentCategory: Category): List<List<Category>> {
        val lists = if (uiState.value.categoryTypeToShow == CategoryType.Expense) {
            uiState.value.expenseSubcategoryLists
        } else {
            uiState.value.incomeSubcategoryLists
        }
        val newLists = mutableListOf<List<Category>>()

        for (i in 1..lists.size) {
            if (i != parentCategory.orderNum) {
                newLists.add(lists[i - 1])
            }
        }

        return newLists
    }

    private fun deleteParentCategoryFromList(categoryToDelete: Category): Pair<List<Category>, List<List<Category>>> {
        val isExpenseType = uiState.value.categoryTypeToShow == CategoryType.Expense
        val newParentCategoryList = mutableListOf<Category>()
        var stepAfterDeleting = 0

        val list = if (isExpenseType) uiState.value.expenseParentCategoryList else uiState.value.incomeParentCategoryList

        list.forEach { category ->
            if (category.orderNum != categoryToDelete.orderNum) {
                newParentCategoryList.add(category.copy(orderNum = category.orderNum - stepAfterDeleting))
            } else {
                stepAfterDeleting = 1
            }
        }

        val newSubCategoryLists = deleteSubcategoriesOfParentCategory(categoryToDelete)

        return newParentCategoryList to newSubCategoryLists
    }

    private fun deleteSubcategoryFromList(categoryToDelete: Category): List<Category> {
        val newList = mutableListOf<Category>()
        var stepAfterDeleting = 0

        uiState.value.subcategoryList.forEach { category ->
            if (category.orderNum != categoryToDelete.orderNum) {
                newList.add(category.copy(orderNum = category.orderNum - stepAfterDeleting))
            } else {
                stepAfterDeleting = 1
            }
        }

        return  newList
    }

    fun changeCategoryTypeToShow(categoryType: CategoryType) {
        _uiState.update { it.copy(categoryTypeToShow = categoryType) }
    }

    fun applyCategoryList(categoriesUiState: CategoriesUiState, context: Context) {
        if (
            categoriesUiState.parentCategories.expense.isNotEmpty() &&
            categoriesUiState.parentCategories.income.isNotEmpty()
        ) {
            _uiState.update {
                it.copy(
                    navigatedFromSetupCategoriesScreen = true,
                    subcategoryList = emptyList(),
                    expenseParentCategoryList = categoriesUiState.parentCategories.expense,
                    expenseSubcategoryLists = categoriesUiState.subcategories.expense,
                    incomeParentCategoryList = categoriesUiState.parentCategories.income,
                    incomeSubcategoryLists = categoriesUiState.subcategories.income
                )
            }
        } else if (uiState.value.expenseParentCategoryList.isEmpty()) {
            reapplyCategoryLists(context)
        }
    }

    fun reapplyCategoryLists(context: Context) {
        val categoryLists = getDefaultCategoriesPackage(context)
        _uiState.update { it.copy(
            navigatedFromSetupCategoriesScreen = true,
            subcategoryList = emptyList(),
            expenseParentCategoryList = categoryLists.expenseParentCategoryList,
            expenseSubcategoryLists = categoryLists.expenseSubcategoryLists,
            incomeParentCategoryList = categoryLists.incomeParentCategoryList,
            incomeSubcategoryLists = categoryLists.incomeSubcategoryLists
        ) }
    }

    fun applySubcategoryList(parentCategoryOrderNum: Int) {
        val subcategoryList = getSubcategoryListByOrderNum(parentCategoryOrderNum)

        if (uiState.value.parentCategoryOrderNum != parentCategoryOrderNum) {
            _uiState.update { it.copy(
                subcategoryList = subcategoryList,
                parentCategoryOrderNum = parentCategoryOrderNum
            ) }
        } else {
            _uiState.update { it.copy(subcategoryList = subcategoryList) }
        }
    }

    fun swapParentCategories(firstOrderNum: Int, secondOrderNum: Int) {
        val categoryList = if (uiState.value.categoryTypeToShow == CategoryType.Expense) {
            uiState.value.expenseParentCategoryList
        } else {
            uiState.value.incomeParentCategoryList
        }
        val firstCategory = getCategoryFromListByOrderNum(categoryList, firstOrderNum)
        val secondCategory = getCategoryFromListByOrderNum(categoryList, secondOrderNum)

        if (firstCategory == null || secondCategory == null) {
            return
        }

        val newList = mutableListOf<Category>()

        categoryList.forEach { category ->
            if (category.orderNum != firstCategory.orderNum && category.orderNum != secondCategory.orderNum) {
                newList.add(category)
            } else if (category.orderNum == firstCategory.orderNum) {
                newList.add(secondCategory.copy(orderNum = category.orderNum))
            } else {
                newList.add(firstCategory.copy(orderNum = category.orderNum))
            }
        }

        if (uiState.value.categoryTypeToShow == CategoryType.Expense) {
            _uiState.update { it.copy(
                expenseParentCategoryList = newList,
                expenseSubcategoryLists = getSwappedSubcategoryLists(firstOrderNum, secondOrderNum)
            ) }
        } else {
            _uiState.update { it.copy(incomeParentCategoryList = newList) }
        }
    }

    fun swapSubcategories(firstOrderNum: Int, secondOrderNum: Int) {
        val subcategoryList = uiState.value.subcategoryList
        val firstCategory = getCategoryFromListByOrderNum(subcategoryList, firstOrderNum)
        val secondCategory = getCategoryFromListByOrderNum(subcategoryList, secondOrderNum)

        if (firstCategory == null || secondCategory == null) {
            return
        }

        val newSubcategoryList = mutableListOf<Category>()
        subcategoryList.forEach { category ->
            if (category.orderNum != firstCategory.orderNum && category.orderNum != secondCategory.orderNum) {
                newSubcategoryList.add(category)
            } else if (category.orderNum == firstCategory.orderNum) {
                newSubcategoryList.add(secondCategory.copy(orderNum = category.orderNum))
            } else {
                newSubcategoryList.add(firstCategory.copy(orderNum = category.orderNum))
            }
        }

        _uiState.update { it.copy(subcategoryList = newSubcategoryList) }
    }

    fun addNewParentCategory(name: String) {
        if (uiState.value.categoryTypeToShow == CategoryType.Expense) {
            val categoryList = uiState.value.expenseParentCategoryList.toMutableList()
            categoryList.add(getNewCategory(
                    type = CategoryType.Expense,
                    rank = CategoryRank.Parent,
                    listSize = uiState.value.expenseParentCategoryList.size,
                    parentCategoryId = null,
                    name = name
                ))
            val subcategoryLists = uiState.value.expenseSubcategoryLists.toMutableList()
            subcategoryLists.add(emptyList())
            _uiState.update { it.copy(
                expenseParentCategoryList = categoryList,
                expenseSubcategoryLists = subcategoryLists
            ) }
        } else {
            val categoryList = uiState.value.incomeParentCategoryList.toMutableList()
            categoryList.add(getNewCategory(
                    type = CategoryType.Income,
                    rank = CategoryRank.Parent,
                    listSize = uiState.value.incomeParentCategoryList.size,
                    parentCategoryId = null,
                    name = name
                ))
            val subcategoryLists = uiState.value.incomeSubcategoryLists.toMutableList()
            subcategoryLists.add(emptyList())
            _uiState.update { it.copy(
                incomeParentCategoryList = categoryList,
                incomeSubcategoryLists = subcategoryLists
            ) }
        }
    }

    fun addNewSubcategory(name: String) {
        val parentCategory = getCategoryFromListByOrderNum(
            getCurrentParentCategoryListByType(),
            uiState.value.parentCategoryOrderNum
        ) ?: return

        val subcategoryList = uiState.value.subcategoryList.toMutableList()
        subcategoryList.add(
            getNewCategory(
                type = uiState.value.categoryTypeToShow,
                rank = CategoryRank.Sub,
                listSize = uiState.value.subcategoryList.size,
                parentCategoryId = parentCategory.id,
                name = name,
                colorName = parentCategory.colorName
            )
        )

        if (subcategoryList.size == 1) {
            if (uiState.value.categoryTypeToShow == CategoryType.Expense) {
                _uiState.update { it.copy(
                    expenseParentCategoryList = changeParentCategoryIdToParentCategory(),
                    subcategoryList = subcategoryList
                ) }
            } else {
                _uiState.update { it.copy(
                    incomeParentCategoryList = changeParentCategoryIdToParentCategory(),
                    subcategoryList = subcategoryList
                ) }
            }
        } else {
            _uiState.update { it.copy(subcategoryList = subcategoryList) }
        }
    }

    fun saveSubcategoryList() {
        if (uiState.value.categoryTypeToShow == CategoryType.Expense) {
            val subcategoryLists = uiState.value.expenseSubcategoryLists.toMutableList()
            subcategoryLists[uiState.value.parentCategoryOrderNum - 1] = uiState.value.subcategoryList
            _uiState.update { it.copy(
                subcategoryList = emptyList(),
                expenseSubcategoryLists = subcategoryLists
            ) }
        } else {
            val subcategoryLists = uiState.value.incomeSubcategoryLists.toMutableList()
            subcategoryLists[uiState.value.parentCategoryOrderNum - 1] = uiState.value.subcategoryList
            _uiState.update { it.copy(
                subcategoryList = emptyList(),
                incomeSubcategoryLists = subcategoryLists
            ) }
        }
    }

    fun applyCategoryToEdit(categoryOrderNum: Int) {
        val isParentCategory = uiState.value.subcategoryList.isEmpty()
        val isExpenseType = uiState.value.categoryTypeToShow == CategoryType.Expense

        val list = if (isParentCategory) {
            if (isExpenseType) uiState.value.expenseParentCategoryList
            else uiState.value.incomeParentCategoryList
        } else {
            uiState.value.subcategoryList
        }

        getCategoryFromListByOrderNum(list, categoryOrderNum)?.let { category ->
            _editCategoryUiState.update {
                it.copy(
                    category = category,
                    showDeleteCategoryButton = (
                        category.type == '-' && category.id != 12 && category.id != 66
                    ) || (
                        category.type == '+' && category.id != 77
                    ),
                    allowSaving = category.name.isNotBlank()
                            && category.iconName.isNotBlank()
                            && category.colorName.isNotBlank()
                )
            }
        }
        _uiState.update {
            it.copy(navigatedFromSetupCategoriesScreen = false)
        }
    }

    fun onCategoryNameChange(name: String) {
        _editCategoryUiState.update {
            it.copy(
                category = it.category?.copy(name = name),
                allowSaving = name.isNotBlank()
            )
        }
    }

    fun onCategoryIconChange(iconName: String) {
        _editCategoryUiState.update {
            it.copy(
                category = it.category?.copy(iconName = iconName)
            )
        }
    }

    fun onCategoryColorChange(color: String) {
        _editCategoryUiState.update {
            it.copy(
                category = it.category?.copy(colorName = color)
            )
        }
    }

    fun saveEditedCategory() {
        val categoryToSave = editCategoryUiState.value.category ?: return

        val isParentCategory = uiState.value.subcategoryList.isEmpty()
        val isExpenseType = uiState.value.categoryTypeToShow == CategoryType.Expense

        val newList = saveEditedCategoryToList(categoryToSave)

        if (isParentCategory) {
            val newSubcategoryLists = getSubcategoryListWithUpdatedColor(categoryToSave)
            if (isExpenseType) {
                _uiState.update { it.copy(
                    expenseParentCategoryList = newList,
                    expenseSubcategoryLists = newSubcategoryLists
                        ?: uiState.value.expenseSubcategoryLists
                ) }
            } else {
                _uiState.update { it.copy(
                    incomeParentCategoryList = newList,
                    incomeSubcategoryLists = newSubcategoryLists
                        ?: uiState.value.incomeSubcategoryLists
                ) }
            }
        } else {
            _uiState.update { it.copy(subcategoryList = newList) }
        }
    }

    private fun getSubcategoryListWithUpdatedColor(category: Category): List<List<Category>>? {
        return category.getCategoryType()?.let {
            getSubcategoryListsByType(it)
        }?.map { list ->
            if (
                list.firstOrNull()?.let{
                    it.parentCategoryId == category.id && it.colorName != category.colorName
                } == true
            ) {
                list.map { subcategory ->
                    subcategory.copy(colorName = category.colorName)
                }
            } else {
                list
            }
        }
    }

    private fun getSubcategoryListsByType(type: CategoryType): List<List<Category>> {
        return if (type == CategoryType.Expense) {
            uiState.value.expenseSubcategoryLists
        } else {
            uiState.value.incomeSubcategoryLists
        }
    }

    private fun deleteParentCategory() {
        val categoryDelete = editCategoryUiState.value.category ?: return
        val isExpenseType = uiState.value.categoryTypeToShow == CategoryType.Expense

        val newListsPair = deleteParentCategoryFromList(categoryDelete)

        if (isExpenseType) {
            _uiState.update { it.copy(
                expenseParentCategoryList = newListsPair.first,
                expenseSubcategoryLists = newListsPair.second
            ) }
        } else {
            _uiState.update { it.copy(
                incomeParentCategoryList = newListsPair.first,
                incomeSubcategoryLists = newListsPair.second
            ) }
        }
    }

    private fun deleteSubcategory() {
        val categoryDelete = editCategoryUiState.value.category ?: return

        val newList = deleteSubcategoryFromList(categoryDelete)

        _uiState.update {
            it.copy(subcategoryList = newList)
        }
    }

    fun deleteCategory() {
        editCategoryUiState.value.category?.let {
            if (
                (it.type == '-' && (it.id == 12 || it.id == 66)) ||
                (it.type == '+' && it.id == 77)
            ) return
        }

        if (uiState.value.subcategoryList.isEmpty()) {
            deleteParentCategory()
        } else {
            deleteSubcategory()
        }
    }

    fun getAllCategories(): List<Category> {
        return CategoryController().concatenateCategoryLists(
            ParentCategoriesLists(
                uiState.value.expenseParentCategoryList,
                uiState.value.incomeParentCategoryList
            ),
            SubcategoriesLists(
                uiState.value.expenseSubcategoryLists,
                uiState.value.incomeSubcategoryLists
            )
        )
    }

    fun changeNavigatedFromSetupCategoriesScreen(value: Boolean) {
        _uiState.update { it.copy(
            navigatedFromSetupCategoriesScreen = value,
            subcategoryList = emptyList()
        ) }
    }

}

data class SetupCategoriesUiState(
    val navigatedFromSetupCategoriesScreen: Boolean = true,
    val categoryTypeToShow: CategoryType = CategoryType.Expense,
    val parentCategoryOrderNum: Int = 0,
    val subcategoryList: List<Category> = emptyList(),

    val expenseParentCategoryList: List<Category> = emptyList(),
    val expenseSubcategoryLists: List<List<Category>> = emptyList(),
    val incomeParentCategoryList: List<Category> = emptyList(),
    val incomeSubcategoryLists: List<List<Category>> = emptyList()
)

data class EditCategoryUiState(
    val category: Category? = null,
    val showDeleteCategoryButton: Boolean = false,
    val allowSaving: Boolean = false
)