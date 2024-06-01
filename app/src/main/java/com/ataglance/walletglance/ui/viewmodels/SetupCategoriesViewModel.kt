package com.ataglance.walletglance.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.ataglance.walletglance.domain.entities.Category
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
        uiState.value.subcategoryList.forEach { category ->
            if (category.id > maxFoundedId) {
                maxFoundedId = category.id
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

    private fun changeParentCategoryIdToParentCategoryOrNull(): List<Category> {
        val orderNum = uiState.value.parentCategoryOrderNum
        val categoryList = getCurrentParentCategoryListByType().toMutableList()

        categoryList.getOrNull(orderNum - 1)?.let {
            categoryList[orderNum - 1] = it.copy(
                parentCategoryId = if (uiState.value.subcategoryList.isEmpty()) null else it.id
            )
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

        return newList
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
        val categoryLists = DefaultCategoriesPackage(context).getDefaultCategories()

        _uiState.update { it.copy(
            navigatedFromSetupCategoriesScreen = true,
            subcategoryList = emptyList(),
            expenseParentCategoryList = categoryLists.parentCategories.expense,
            expenseSubcategoryLists = categoryLists.subcategories.expense,
            incomeParentCategoryList = categoryLists.parentCategories.income,
            incomeSubcategoryLists = categoryLists.subcategories.income
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
            list = getCurrentParentCategoryListByType(),
            orderNum = uiState.value.parentCategoryOrderNum
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

        _uiState.update { it.copy(subcategoryList = subcategoryList) }
    }

    fun saveSubcategoryList() {
        if (uiState.value.categoryTypeToShow == CategoryType.Expense) {
            val subcategoryLists = uiState.value.expenseSubcategoryLists.toMutableList()
            subcategoryLists[uiState.value.parentCategoryOrderNum - 1] = uiState.value.subcategoryList
            _uiState.update {
                it.copy(
                    subcategoryList = emptyList(),
                    expenseSubcategoryLists = subcategoryLists,
                    expenseParentCategoryList = changeParentCategoryIdToParentCategoryOrNull()
                )
            }
        } else {
            val subcategoryLists = uiState.value.incomeSubcategoryLists.toMutableList()
            subcategoryLists[uiState.value.parentCategoryOrderNum - 1] = uiState.value.subcategoryList
            _uiState.update {
                it.copy(
                    subcategoryList = emptyList(),
                    incomeSubcategoryLists = subcategoryLists,
                    incomeParentCategoryList = changeParentCategoryIdToParentCategoryOrNull()
                )
            }
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