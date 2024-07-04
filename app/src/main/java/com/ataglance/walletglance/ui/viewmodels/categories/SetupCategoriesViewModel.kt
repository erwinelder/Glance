package com.ataglance.walletglance.ui.viewmodels.categories

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ataglance.walletglance.R
import com.ataglance.walletglance.data.categories.CategoriesWithSubcategories
import com.ataglance.walletglance.data.categories.Category
import com.ataglance.walletglance.data.categories.CategoryRank
import com.ataglance.walletglance.data.categories.CategoryType
import com.ataglance.walletglance.data.categories.CategoryWithSubcategories
import com.ataglance.walletglance.data.categories.color.CategoryColorWithName
import com.ataglance.walletglance.data.categories.color.CategoryColors
import com.ataglance.walletglance.data.categories.color.CategoryPossibleColors
import com.ataglance.walletglance.data.categories.icons.CategoryIcon
import com.ataglance.walletglance.ui.utils.toCategoryColorWithName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SetupCategoriesViewModel(
    private val passedCategoriesWithSubcategories: CategoriesWithSubcategories
) : ViewModel() {
    private val _uiState: MutableStateFlow<SetupCategoriesUiState> = MutableStateFlow(
        SetupCategoriesUiState(categoriesWithSubcategories = passedCategoriesWithSubcategories)
    )
    val uiState: StateFlow<SetupCategoriesUiState> = _uiState.asStateFlow()

    private val _editCategoryUiState = MutableStateFlow(EditCategoryUiState())
    val editCategoryUiState = _editCategoryUiState.asStateFlow()

    private fun getNewCategoryId(): Int {
        return ((uiState.value.categoriesWithSubcategories.concatenateAsCategoryList() +
                uiState.value.subcategoryList)
                    .maxOfOrNull { it.id } ?: 0) + 1
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

    fun changeCategoryTypeToShow(categoryType: CategoryType) {
        _uiState.update { it.copy(categoryType = categoryType) }
    }

    fun reapplyCategoryLists() {
        _uiState.update { it.copy(
            categoryWithSubcategories = null,
            categoriesWithSubcategories = passedCategoriesWithSubcategories,
        ) }
    }

    fun applySubcategoryListToEdit(categoryWithSubcategories: CategoryWithSubcategories) {
        _uiState.update {
            it.copy(categoryWithSubcategories = categoryWithSubcategories)
        }
    }

    fun clearSubcategoryList() {
        _uiState.update {
            it.copy(categoryWithSubcategories = null)
        }
    }

    fun swapParentCategories(firstOrderNum: Int, secondOrderNum: Int) {
        val categoryWithSubcategoriesList = uiState.value.categoriesWithSubcategories
            .getByType(uiState.value.categoryType)
        val twoCategoriesWithSubcategories = categoryWithSubcategoriesList.let {
                (it.getOrNull(firstOrderNum - 1) ?: return) to
                        (it.getOrNull(secondOrderNum - 1) ?: return)
            }

        val newCategoryWithSubcategoriesList = categoryWithSubcategoriesList.toMutableList()
        newCategoryWithSubcategoriesList[firstOrderNum - 1] =
            twoCategoriesWithSubcategories.second.copy(
                category = twoCategoriesWithSubcategories.second
                    .category.copy(orderNum = firstOrderNum)
            )
        newCategoryWithSubcategoriesList[secondOrderNum - 1] =
            twoCategoriesWithSubcategories.first.copy(
                category = twoCategoriesWithSubcategories.first
                    .category.copy(orderNum = secondOrderNum)
            )

        _uiState.update {
            it.copy(
                categoriesWithSubcategories = it.categoriesWithSubcategories.replaceListByType(
                    list = newCategoryWithSubcategoriesList,
                    type = uiState.value.categoryType
                )
            )
        }
    }

    fun swapSubcategories(firstOrderNum: Int, secondOrderNum: Int) {
        val categoryWithSubcategories = uiState.value.categoryWithSubcategories ?: return
        val twoCategories = categoryWithSubcategories.subcategoryList.let {
            (it.getOrNull(firstOrderNum - 1) ?: return) to
                    (it.getOrNull(secondOrderNum - 1) ?: return)
        }

        val subcategoryList = categoryWithSubcategories.subcategoryList.toMutableList()
        subcategoryList[firstOrderNum - 1] = twoCategories.second.copy(orderNum = firstOrderNum)
        subcategoryList[secondOrderNum - 1] = twoCategories.first.copy(orderNum = secondOrderNum)

        _uiState.update {
            it.copy(
                categoryWithSubcategories = categoryWithSubcategories
                    .copy(subcategoryList = subcategoryList)
            )
        }
    }

    fun addNewParentCategory(context: Context) {
        _uiState.update { uiState ->
            uiState.copy(
                categoriesWithSubcategories = uiState.categoriesWithSubcategories
                    .appendNewCategoryByType(
                        category = getNewCategory(
                            rank = CategoryRank.Parent,
                            listSize = uiState.categoriesWithSubcategories
                                .getByType(uiState.categoryType).size,
                            parentCategoryId = null,
                            name = context.getString(R.string.new_category_name)
                        ),
                        type = uiState.categoryType
                    )
            )
        }
    }

    fun addNewSubcategory(context: Context) {
        val categoryWithSubcategories = uiState.value.categoryWithSubcategories ?: return

        _uiState.update { uiState ->
            uiState.copy(
                categoryWithSubcategories = categoryWithSubcategories.appendSubcategory(
                    subcategory = getNewCategory(
                        rank = CategoryRank.Sub,
                        listSize = categoryWithSubcategories.subcategoryList.size,
                        parentCategoryId = categoryWithSubcategories.category.id,
                        name = context.getString(R.string.new_category_name)
                    )
                )
            )
        }
    }

    fun saveSubcategoryList() {
        val state = uiState.value
        state.categoryWithSubcategories ?: return

        val categoryWithSubcategoriesList = state.categoriesWithSubcategories
            .getByType(state.categoryType).map { item ->
                item.takeIf {
                    it.category.id != state.categoryWithSubcategories.category.id
                } ?: state.categoryWithSubcategories
            }

        state.categoriesWithSubcategories.replaceListByType(
            list = categoryWithSubcategoriesList,
            type = state.categoryType
        ).let { newCategoriesWithSubcategories ->
            _uiState.update {
                it.copy(
                    categoryWithSubcategories = null,
                    categoriesWithSubcategories = newCategoriesWithSubcategories
                )
            }
        }
    }

    fun applyCategoryToEdit(category: Category) {
        _editCategoryUiState.update {
            it.copy(
                category = category,
                showDeleteCategoryButton = category.canBeDeleted(),
                allowSaving = category.name.isNotBlank()
            )
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

    private fun saveEditedParentCategory() {
        val editedCategory = editCategoryUiState.value.category ?: return

        _uiState.update {
            it.copy(
                categoriesWithSubcategories = it.categoriesWithSubcategories.replaceCategory(
                    category = editedCategory,
                    type = uiState.value.categoryType
                )
            )
        }
    }

    private fun saveEditedSubcategory() {
        val editedCategory = editCategoryUiState.value.category ?: return

        _uiState.update {
            it.copy(
                categoryWithSubcategories = it.categoryWithSubcategories
                    ?.replaceSubcategory(editedCategory)
            )
        }
    }

    fun saveEditedCategory() {
        if (uiState.value.categoryWithSubcategories == null) {
            saveEditedParentCategory()
        } else {
            saveEditedSubcategory()
        }
    }

    private fun deleteParentCategoryById(category: Category) {
        _uiState.update {
            it.copy(
                categoriesWithSubcategories = it.categoriesWithSubcategories
                    .deleteCategoryById(category)
            )
        }
    }

    private fun deleteSubcategoryById(id: Int) {

        _uiState.update {
            it.copy(
                categoryWithSubcategories = it.categoryWithSubcategories?.deleteSubcategoryById(id)
            )
        }
    }

    fun deleteCategory() {
        val categoryToDelete = editCategoryUiState.value.category
            ?.takeIf { it.canBeDeleted() } ?: return

        if (uiState.value.categoryWithSubcategories == null) {
            deleteParentCategoryById(categoryToDelete)
        } else {
            deleteSubcategoryById(categoryToDelete.id)
        }
    }

    fun getAllCategories(): List<Category> {
        return uiState.value.categoriesWithSubcategories.concatenateAsCategoryList()
    }

}

data class SetupCategoriesViewModelFactory(
    private val categoriesWithSubcategories: CategoriesWithSubcategories
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SetupCategoriesViewModel(categoriesWithSubcategories) as T
    }
}


data class SetupCategoriesUiState(
    val categoryType: CategoryType = CategoryType.Expense,
    val parentCategoryOrderNum: Int = 0,
    val subcategoryList: List<Category> = emptyList(),

    val categoryWithSubcategories: CategoryWithSubcategories? = null,

    val categoriesWithSubcategories: CategoriesWithSubcategories = CategoriesWithSubcategories()
) {

    fun getCategoriesWithSubcategoriesListByType(): List<CategoryWithSubcategories> {
        return categoriesWithSubcategories.getByType(categoryType)
    }

}

data class EditCategoryUiState(
    val category: Category? = null,
    val showDeleteCategoryButton: Boolean = false,
    val allowSaving: Boolean = false
)