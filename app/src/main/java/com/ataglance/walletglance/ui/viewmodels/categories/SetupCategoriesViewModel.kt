package com.ataglance.walletglance.ui.viewmodels.categories

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ataglance.walletglance.R
import com.ataglance.walletglance.data.categories.CategoriesLists
import com.ataglance.walletglance.data.categories.Category
import com.ataglance.walletglance.data.categories.CategoryRank
import com.ataglance.walletglance.data.categories.CategoryType
import com.ataglance.walletglance.data.categories.ParentCategoriesLists
import com.ataglance.walletglance.data.categories.SubcategoriesLists
import com.ataglance.walletglance.data.categories.color.CategoryColorWithName
import com.ataglance.walletglance.data.categories.color.CategoryColors
import com.ataglance.walletglance.data.categories.color.CategoryPossibleColors
import com.ataglance.walletglance.data.categories.icons.CategoryIcon
import com.ataglance.walletglance.ui.utils.findByRecordNum
import com.ataglance.walletglance.ui.utils.toCategoryColorWithName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SetupCategoriesViewModel(
    private val categoriesLists: CategoriesLists
) : ViewModel() {
    private val _uiState: MutableStateFlow<SetupCategoriesUiState> = MutableStateFlow(
        SetupCategoriesUiState(
            subcategoryList = emptyList(),
            expenseParentCategoryList = categoriesLists.parentCategories.expense,
            expenseSubcategoryLists = categoriesLists.subcategories.expense,
            incomeParentCategoryList = categoriesLists.parentCategories.income,
            incomeSubcategoryLists = categoriesLists.subcategories.income
        )
    )
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
        rank: CategoryRank,
        listSize: Int,
        parentCategoryId: Int?,
        name: String,
        colorWithName: CategoryColorWithName = CategoryColors.GrayDefault.toCategoryColorWithName()
    ): Category {
        return Category(
            id = getNewCategoryId(),
            type = uiState.value.categoryType,
            rank = rank,
            orderNum = listSize + 1,
            parentCategoryId = parentCategoryId,
            name = name,
            icon = CategoryIcon.Other,
            colorWithName = colorWithName
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
        _uiState.update { it.copy(categoryType = categoryType) }
    }

    fun reapplyCategoryLists() {
        _uiState.update { it.copy(
            subcategoryList = emptyList(),
            expenseParentCategoryList = categoriesLists.parentCategories.expense,
            expenseSubcategoryLists = categoriesLists.subcategories.expense,
            incomeParentCategoryList = categoriesLists.parentCategories.income,
            incomeSubcategoryLists = categoriesLists.subcategories.income
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

    fun clearSubcategoryList() {
        _uiState.update {
            it.copy(
                subcategoryList = listOf()
            )
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

    fun addNewParentCategory(context: Context) {
        val categoryList = uiState.value.getParentCategoryListByCurrentType().toMutableList()
        categoryList.add(
            getNewCategory(
                rank = CategoryRank.Parent,
                listSize = categoryList.size,
                parentCategoryId = null,
                name = context.getString(R.string.new_category_name)
            )
        )

        val subcategoryLists = uiState.value.getSubcategoryListsByCurrentType().toMutableList()
        subcategoryLists.add(emptyList())

        _uiState.update {
            if (it.currentTypeIsExpense()) {
                it.copy(
                    expenseParentCategoryList = categoryList,
                    expenseSubcategoryLists = subcategoryLists
                )
            } else {
                it.copy(
                    incomeParentCategoryList = categoryList,
                    incomeSubcategoryLists = subcategoryLists
                )
            }
        }
    }

    fun addNewSubcategory(context: Context) {
        val parentCategory = uiState.value
            .getParentCategoryListByCurrentType()
            .findByRecordNum(uiState.value.parentCategoryOrderNum) ?: return

        val subcategoryList = uiState.value.subcategoryList.toMutableList()
        subcategoryList.add(
            getNewCategory(
                rank = CategoryRank.Sub,
                listSize = uiState.value.subcategoryList.size,
                parentCategoryId = parentCategory.id,
                name = context.getString(R.string.new_subcategory_name),
                colorWithName = parentCategory.colorWithName
            )
        )

        _uiState.update { it.copy(subcategoryList = subcategoryList) }
    }

    fun saveSubcategoryList() {
        val subcategoryLists = uiState.value.getSubcategoryListsByCurrentType().toMutableList()
        subcategoryLists[uiState.value.parentCategoryOrderNum - 1] = uiState.value.subcategoryList

        _uiState.update {
            if (uiState.value.currentTypeIsExpense()) {
                it.copy(
                    subcategoryList = emptyList(),
                    expenseSubcategoryLists = subcategoryLists,
                    expenseParentCategoryList = changeParentCategoryIdToParentCategoryOrNull()
                )
            } else {
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
                    )
                }
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

    fun onCategoryIconChange(icon: CategoryIcon) {
        _editCategoryUiState.update {
            it.copy(
                category = it.category?.copy(icon = icon)
            )
        }
    }

    fun onCategoryColorChange(colorName: String) {
        _editCategoryUiState.update {
            it.copy(
                category = it.category?.copy(
                    colorWithName = CategoryPossibleColors().getByName(colorName)
                )
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
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        incomeParentCategoryList = newList,
                        incomeSubcategoryLists = newSubcategoryLists
                    )
                }
            }
        } else {
            _uiState.update { it.copy(subcategoryList = newList) }
        }
    }

    private fun updateSubcategoriesColorIfNeeded(category: Category): List<List<Category>> {
        return uiState.value.getSubcategoryListsByType(category.type).map { list ->
            list.takeUnless {
                list.firstOrNull()?.let {
                    it.parentCategoryId == category.id &&
                            it.colorWithName.name != category.colorWithName.name
                } == true
            } ?: list.map { it.copy(colorWithName = category.colorWithName) }
        }
    }

    private fun deleteParentCategory() {
        val categoryDelete = editCategoryUiState.value.category ?: return
        val isExpenseType = uiState.value.categoryType == CategoryType.Expense

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
                (it.isExpense() && (it.id == 12 || it.id == 66)) ||
                (it.isIncome() && it.id == 77)
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

}

data class SetupCategoriesViewModelFactory(
    private val categoriesLists: CategoriesLists
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SetupCategoriesViewModel(categoriesLists) as T
    }
}


data class SetupCategoriesUiState(
    val categoryType: CategoryType = CategoryType.Expense,
    val parentCategoryOrderNum: Int = 0,
    val subcategoryList: List<Category> = emptyList(),

    val expenseParentCategoryList: List<Category> = emptyList(),
    val expenseSubcategoryLists: List<List<Category>> = emptyList(),
    val incomeParentCategoryList: List<Category> = emptyList(),
    val incomeSubcategoryLists: List<List<Category>> = emptyList()
) {

    fun currentTypeIsExpense(): Boolean {
        return categoryType == CategoryType.Expense
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