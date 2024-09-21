package com.ataglance.walletglance.category.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ataglance.walletglance.category.data.local.model.CategoryEntity
import com.ataglance.walletglance.category.data.mapper.toCategoryEntityList
import com.ataglance.walletglance.category.domain.CategoriesWithSubcategories
import com.ataglance.walletglance.category.domain.Category
import com.ataglance.walletglance.category.domain.CategoryType
import com.ataglance.walletglance.category.domain.CategoryWithSubcategories
import com.ataglance.walletglance.category.domain.color.CategoryColors
import com.ataglance.walletglance.category.domain.icons.CategoryIcon
import com.ataglance.walletglance.category.utils.toCategoryColorWithName
import com.ataglance.walletglance.core.utils.moveItems
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EditCategoriesViewModel(
    private val passedCategoriesWithSubcategories: CategoriesWithSubcategories
) : ViewModel() {
    private val _uiState: MutableStateFlow<SetupCategoriesUiState> = MutableStateFlow(
        SetupCategoriesUiState(categoriesWithSubcategories = passedCategoriesWithSubcategories)
    )
    val uiState: StateFlow<SetupCategoriesUiState> = _uiState.asStateFlow()


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

    fun moveParentCategories(firstIndex: Int, secondIndex: Int) {
        val categoryWithSubcategoriesList = uiState.value.categoriesWithSubcategories
            .getByType(uiState.value.categoryType)
            .moveItems(firstIndex, secondIndex)

        _uiState.update {
            it.copy(
                categoriesWithSubcategories = it.categoriesWithSubcategories.replaceListByType(
                    list = categoryWithSubcategoriesList,
                    type = uiState.value.categoryType
                )
            )
        }
    }

    fun moveSubcategories(firstIndex: Int, secondIndex: Int) {
        val categoryWithSubcategories = uiState.value.categoryWithSubcategories ?: return
        val subcategoryList = categoryWithSubcategories.subcategoryList
            .moveItems(firstIndex, secondIndex)

        _uiState.update {
            it.copy(
                categoryWithSubcategories = categoryWithSubcategories.copy(
                    subcategoryList = subcategoryList
                )
            )
        }
    }

    fun getNewParentCategory(): Category {
        return Category(
            type = uiState.value.categoryType,
            parentCategoryId = null,
            name = "",
            icon = CategoryIcon.Other,
            colorWithName = CategoryColors.GrayDefault.toCategoryColorWithName()
        )
    }

    fun getNewSubcategory(): Category {
        val parentCategory = uiState.value.categoryWithSubcategories?.category ?: Category()

        return Category(
            type = uiState.value.categoryType,
            parentCategoryId = parentCategory.id,
            name = "",
            icon = parentCategory.icon,
            colorWithName = parentCategory.colorWithName
        )
    }

    fun saveSubcategoryList() {
        val state = uiState.value
        state.categoryWithSubcategories ?: return

        val categoryWithSubcategoriesList = state.categoriesWithSubcategories
            .getByType(state.categoryType)
            .map { item ->
                item.takeIf { it.category.id != state.categoryWithSubcategories.category.id }
                    ?: state.categoryWithSubcategories.fixSubcategoriesOrderNumbers()
            }

        val newCategoriesWithSubcategories = state.categoriesWithSubcategories.replaceListByType(
            list = categoryWithSubcategoriesList,
            type = state.categoryType
        )
        _uiState.update {
            it.copy(
                categoryWithSubcategories = null,
                categoriesWithSubcategories = newCategoriesWithSubcategories
            )
        }
    }

    fun deleteCategory(category: Category) {
        if (!category.canBeDeleted()) return

        if (uiState.value.categoryWithSubcategories == null) {
            deleteParentCategory(category)
        } else {
            deleteSubcategoryById(category.id)
        }
    }

    private fun deleteParentCategory(category: Category) {
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

    fun saveEditedCategory(category: Category) {
        _uiState.update {
            it.saveCategory(category)
        }
    }

    fun getAllCategoryEntities(): List<CategoryEntity> {
        return uiState.value.categoriesWithSubcategories
            .fixParentCategoriesOrderNums()
            .concatenateAsCategoryList()
            .toCategoryEntityList()
    }

}

data class SetupCategoriesViewModelFactory(
    private val categoriesWithSubcategories: CategoriesWithSubcategories
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditCategoriesViewModel(categoriesWithSubcategories) as T
    }
}


data class SetupCategoriesUiState(
    val categoryType: CategoryType = CategoryType.Expense,
    val categoryWithSubcategories: CategoryWithSubcategories? = null,
    val categoriesWithSubcategories: CategoriesWithSubcategories = CategoriesWithSubcategories()
) {

    private fun getNewCategoryId(): Int {
        return ((categoriesWithSubcategories.concatenateAsCategoryList() +
                (categoryWithSubcategories?.subcategoryList ?: emptyList()))
            .maxOfOrNull { it.id } ?: 0) + 1
    }

    fun saveCategory(category: Category): SetupCategoriesUiState {
        return if (categoryWithSubcategories == null) {
            saveParentCategory(category)
        } else {
            saveSubcategory(category)
        }
    }

    private fun saveParentCategory(category: Category): SetupCategoriesUiState {
        return this.copy(
            categoriesWithSubcategories = if (category.id == 0) {
                categoriesWithSubcategories.appendNewCategory(
                    category = category.copy(id = getNewCategoryId())
                )
            } else {
                categoriesWithSubcategories.replaceCategory(category)
            }
        )
    }

    private fun saveSubcategory(category: Category): SetupCategoriesUiState {
        return this.copy(
            categoryWithSubcategories = if (category.id == 0) {
                categoryWithSubcategories?.appendNewSubcategory(
                    subcategory = category.copy(id = getNewCategoryId())
                )
            } else {
                categoryWithSubcategories?.replaceSubcategory(category)
            }
        )
    }

}