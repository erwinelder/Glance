package com.ataglance.walletglance.ui.viewmodels.categories

import android.content.Context
import androidx.lifecycle.ViewModel
import com.ataglance.walletglance.data.categories.CategoriesLists
import com.ataglance.walletglance.data.categories.CategoryColors
import com.ataglance.walletglance.data.categories.CategoryRank
import com.ataglance.walletglance.data.categories.CategoryType
import com.ataglance.walletglance.data.categories.DefaultCategoriesPackage
import com.ataglance.walletglance.data.categories.ParentCategoriesLists
import com.ataglance.walletglance.data.categories.SubcategoriesLists
import com.ataglance.walletglance.domain.entities.Category
import com.ataglance.walletglance.ui.utils.findByRecordNum
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SetupCategoriesViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SetupCategoriesUiState())
    val uiState = _uiState.asStateFlow()

    private val _editCategoryUiState = MutableStateFlow(EditCategoryUiState())
    val editCategoryUiState = _editCategoryUiState.asStateFlow()

    private fun getNewCategoryId(): Int {
        var maxFoundedId = 0

        uiState.value.expenseParentCategoryList.maxOfOrNull { it.id }?.let {
            if (it > 0) maxFoundedId = it
        }
        uiState.value.incomeParentCategoryList.maxOfOrNull { it.id }?.let {
            if (it > maxFoundedId) maxFoundedId = it
        }

        uiState.value.expenseSubcategoryLists.forEach { categoryList ->
            categoryList.maxOfOrNull { it.id }?.let {
                if (it > maxFoundedId) maxFoundedId = it
            }
        }
        uiState.value.incomeSubcategoryLists.forEach { categoryList ->
            categoryList.maxOfOrNull { it.id }?.let {
                if (it > maxFoundedId) maxFoundedId = it
            }
        }
        uiState.value.subcategoryList.maxOfOrNull { it.id }?.let {
            if (it > maxFoundedId) maxFoundedId = it
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

    private fun changeParentCategoryIdToParentCategoryOrNull(): List<Category> {
        val orderNum = uiState.value.parentCategoryOrderNum
        val categoryList = uiState.value.getParentCategoryListByCurrentType().toMutableList()

        categoryList.getOrNull(orderNum - 1)?.let {
            categoryList[orderNum - 1] = it.copy(
                parentCategoryId = it.id.takeIf { uiState.value.subcategoryList.isNotEmpty() }
            )
        }

        return categoryList
    }

    private fun getSubcategoryListByOrderNum(parentCategoryOrderNum: Int): List<Category> {
        return uiState.value.getSubcategoryListsByCurrentType()[parentCategoryOrderNum - 1]
    }

    private fun getSwappedSubcategoryLists(
        firstParentOrderNum: Int,
        secondParentOrderNum: Int
    ): List<List<Category>> {
        val subcategoryLists = uiState.value.getSubcategoryListsByCurrentType().toMutableList()
        val firstList = subcategoryLists[firstParentOrderNum - 1]
        val secondList = subcategoryLists[secondParentOrderNum - 1]
        subcategoryLists[secondParentOrderNum - 1] = firstList
        subcategoryLists[firstParentOrderNum - 1] = secondList

        return subcategoryLists
    }

    private fun saveEditedCategoryToList(categoryToSave: Category): List<Category> {
        return uiState.value
            .getParentCategoryListOrEditSubcategoryList()
            .map { if (it.orderNum != categoryToSave.orderNum) it else categoryToSave }
    }

    private fun deleteSubcategoriesOfParentCategory(
        parentCategory: Category
    ): List<List<Category>> {
        val lists = uiState.value.getSubcategoryListsByCurrentType()
        val newLists = mutableListOf<List<Category>>()

        for (i in 1..lists.size) {
            if (i != parentCategory.orderNum) {
                newLists.add(lists[i - 1])
            }
        }

        return newLists
    }

    private fun deleteParentCategoryFromList(
        categoryToDelete: Category
    ): Pair<List<Category>, List<List<Category>>> {
        val newParentCategoryList = mutableListOf<Category>()
        var stepAfterDeleting = 0

        uiState.value.getParentCategoryListByCurrentType().forEach { category ->
            if (category.orderNum != categoryToDelete.orderNum) {
                newParentCategoryList.add(
                    category.copy(orderNum = category.orderNum - stepAfterDeleting)
                )
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

    fun applyCategoryList(categoriesUiState: CategoriesLists, context: Context) {
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
            _uiState.update {
                it.copy(
                    subcategoryList = subcategoryList,
                    parentCategoryOrderNum = parentCategoryOrderNum
                )
            }
        } else {
            _uiState.update { it.copy(subcategoryList = subcategoryList) }
        }
    }

    fun swapParentCategories(firstOrderNum: Int, secondOrderNum: Int) {
        val categoryList = uiState.value.getParentCategoryListByCurrentType()
        val firstCategory = categoryList.findByRecordNum(firstOrderNum) ?: return
        val secondCategory = categoryList.findByRecordNum(secondOrderNum) ?: return

        val newList = categoryList.map { category ->
            if (
                category.orderNum != firstCategory.orderNum &&
                category.orderNum != secondCategory.orderNum
            ) {
                category
            } else if (category.orderNum == firstCategory.orderNum) {
                secondCategory.copy(orderNum = category.orderNum)
            } else {
                firstCategory.copy(orderNum = category.orderNum)
            }
        }

        if (uiState.value.currentTypeIsExpense()) {
            _uiState.update {
                it.copy(
                    expenseParentCategoryList = newList,
                    expenseSubcategoryLists =
                        getSwappedSubcategoryLists(firstOrderNum, secondOrderNum)
                )
            }
        } else {
            _uiState.update { it.copy(incomeParentCategoryList = newList) }
        }
    }

    fun swapSubcategories(firstOrderNum: Int, secondOrderNum: Int) {
        val subcategoryList = uiState.value.subcategoryList
        val firstCategory = subcategoryList.findByRecordNum(firstOrderNum) ?: return
        val secondCategory = subcategoryList.findByRecordNum(secondOrderNum) ?: return

        val newSubcategoryList = subcategoryList.map { category ->
            if (
                category.orderNum != firstCategory.orderNum &&
                category.orderNum != secondCategory.orderNum
            ) {
                category
            } else if (category.orderNum == firstCategory.orderNum) {
                secondCategory.copy(orderNum = category.orderNum)
            } else {
                firstCategory.copy(orderNum = category.orderNum)
            }
        }

        _uiState.update { it.copy(subcategoryList = newSubcategoryList) }
    }

    fun addNewParentCategory(name: String) {
        if (uiState.value.currentTypeIsExpense()) {

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
        val parentCategory = uiState.value
            .getParentCategoryListByCurrentType()
            .findByRecordNum(uiState.value.parentCategoryOrderNum) ?: return

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
        if (uiState.value.currentTypeIsExpense()) {
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

        uiState.value
            .getParentCategoryListOrEditSubcategoryList()
            .findByRecordNum(categoryOrderNum)
            ?.let { category ->
                _editCategoryUiState.update {
                    it.copy(
                        category = category,
                        showDeleteCategoryButton = (
                            category.isExpense() && category.id != 12 && category.id != 66
                        ) || (
                            category.isIncome() && category.id != 77
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
        val newList = saveEditedCategoryToList(categoryToSave)

        if (isParentCategory) {
            val newSubcategoryLists = updateSubcategoriesColorIfNeeded(categoryToSave)
            if (uiState.value.currentTypeIsExpense()) {
                _uiState.update {
                    it.copy(
                        expenseParentCategoryList = newList,
                        expenseSubcategoryLists = newSubcategoryLists
                            ?: uiState.value.expenseSubcategoryLists
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        incomeParentCategoryList = newList,
                        incomeSubcategoryLists = newSubcategoryLists
                            ?: uiState.value.incomeSubcategoryLists
                    )
                }
            }
        } else {
            _uiState.update { it.copy(subcategoryList = newList) }
        }
    }

    private fun updateSubcategoriesColorIfNeeded(category: Category): List<List<Category>>? {
        return category.getCategoryType()
            ?.let { uiState.value.getSubcategoryListsByType(it) }
            ?.map { list ->
                list.takeUnless {
                    list.firstOrNull()?.let {
                        it.parentCategoryId == category.id && it.colorName != category.colorName
                    } == true
                } ?: list.map { it.copy(colorName = category.colorName) }
            }
    }

    private fun deleteParentCategory() {
        val categoryDelete = editCategoryUiState.value.category ?: return
        val isExpenseType = uiState.value.categoryTypeToShow == CategoryType.Expense

        val newListsPair = deleteParentCategoryFromList(categoryDelete)

        if (isExpenseType) {
            _uiState.update {
                it.copy(
                    expenseParentCategoryList = newListsPair.first,
                    expenseSubcategoryLists = newListsPair.second
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    incomeParentCategoryList = newListsPair.first,
                    incomeSubcategoryLists = newListsPair.second
                )
            }
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
        return CategoriesLists(
            ParentCategoriesLists(
                uiState.value.expenseParentCategoryList,
                uiState.value.incomeParentCategoryList
            ),
            SubcategoriesLists(
                uiState.value.expenseSubcategoryLists,
                uiState.value.incomeSubcategoryLists
            )
        ).concatenateLists()
    }

    fun changeNavigatedFromSetupCategoriesScreen(value: Boolean) {
        _uiState.update {
            it.copy(
                navigatedFromSetupCategoriesScreen = value,
                subcategoryList = emptyList()
            )
        }
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
) {

    fun currentTypeIsExpense(): Boolean {
        return categoryTypeToShow == CategoryType.Expense
    }

    fun getParentCategoryListByCurrentType(): List<Category> {
        return if (currentTypeIsExpense()) expenseParentCategoryList else incomeParentCategoryList
    }

    fun getSubcategoryListsByCurrentType(): List<List<Category>> {
        return if (currentTypeIsExpense()) expenseSubcategoryLists else incomeSubcategoryLists
    }

    fun getSubcategoryListsByType(type: CategoryType): List<List<Category>> {
        return if (type == CategoryType.Expense) expenseSubcategoryLists else incomeSubcategoryLists
    }

    fun getParentCategoryListOrEditSubcategoryList(): List<Category> {
        return subcategoryList.ifEmpty {
            getParentCategoryListByCurrentType()
        }
    }

}

data class EditCategoryUiState(
    val category: Category? = null,
    val showDeleteCategoryButton: Boolean = false,
    val allowSaving: Boolean = false
)