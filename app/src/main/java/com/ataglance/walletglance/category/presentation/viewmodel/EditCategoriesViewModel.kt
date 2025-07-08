package com.ataglance.walletglance.category.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.category.domain.model.GroupedCategoriesByType
import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.category.domain.model.CategoryColor
import com.ataglance.walletglance.category.domain.model.CategoryIcon
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.category.domain.model.GroupedCategories
import com.ataglance.walletglance.category.domain.usecase.GetCategoriesUseCase
import com.ataglance.walletglance.category.domain.usecase.SaveCategoriesUseCase
import com.ataglance.walletglance.category.presentation.model.DefaultCategoriesPackage
import com.ataglance.walletglance.category.presentation.model.SetupCategoriesUiState
import com.ataglance.walletglance.core.utils.moveItems
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditCategoriesViewModel(
    defaultCategoriesPackage: DefaultCategoriesPackage,
    private val saveCategoriesUseCase: SaveCategoriesUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    init {
        viewModelScope.launch {
            val categories = getCategoriesUseCase.getGrouped()
                .takeIf { it.expense.isNotEmpty() && it.income.isNotEmpty() }
                ?: defaultCategoriesPackage.get()

            initialGroupedCategoriesByType = categories
            _uiState.update {
                it.copy(groupedCategoriesByType = categories)
            }
        }
    }

    private var initialGroupedCategoriesByType = GroupedCategoriesByType()


    private val _uiState = MutableStateFlow(SetupCategoriesUiState())
    val uiState: StateFlow<SetupCategoriesUiState> = _uiState.asStateFlow()

    fun changeCategoryType(categoryType: CategoryType) {
        _uiState.update {
            it.copy(categoryType = categoryType)
        }
    }

    fun reapplyCategoryLists() {
        _uiState.update {
            it.copy(
                groupedCategories = null,
                groupedCategoriesByType = initialGroupedCategoriesByType,
            )
        }
    }

    fun applySubcategoryListToEdit(groupedCategories: GroupedCategories) {
        _uiState.update {
            it.copy(groupedCategories = groupedCategories)
        }
    }

    fun clearSubcategoryList() {
        _uiState.update {
            it.copy(groupedCategories = null)
        }
    }

    fun moveParentCategories(firstIndex: Int, secondIndex: Int) {
        val categoryWithSubcategoriesList = uiState.value.groupedCategoriesByType
            .getByType(uiState.value.categoryType)
            .moveItems(firstIndex, secondIndex)

        _uiState.update {
            it.copy(
                groupedCategoriesByType = it.groupedCategoriesByType.replaceListByType(
                    list = categoryWithSubcategoriesList,
                    type = uiState.value.categoryType
                )
            )
        }
    }

    fun moveSubcategories(firstIndex: Int, secondIndex: Int) {
        val categoryWithSubcategories = uiState.value.groupedCategories ?: return
        val subcategoryList = categoryWithSubcategories.subcategories
            .moveItems(firstIndex, secondIndex)

        _uiState.update {
            it.copy(
                groupedCategories = categoryWithSubcategories.copy(
                    subcategories = subcategoryList
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
            color = CategoryColor.GrayDefault
        )
    }

    fun getNewSubcategory(): Category {
        val parentCategory = uiState.value.groupedCategories?.category ?: Category()

        return Category(
            type = uiState.value.categoryType,
            parentCategoryId = parentCategory.id,
            name = "",
            icon = parentCategory.icon,
            color = parentCategory.color
        )
    }

    fun saveSubcategoryList() {
        val state = uiState.value
        state.groupedCategories ?: return

        val categoryWithSubcategoriesList = state.groupedCategoriesByType
            .getByType(state.categoryType)
            .map { item ->
                item.takeIf { it.category.id != state.groupedCategories.category.id }
                    ?: state.groupedCategories.fixSubcategoriesOrderNumbers()
            }

        val newCategoriesWithSubcategories = state.groupedCategoriesByType.replaceListByType(
            list = categoryWithSubcategoriesList,
            type = state.categoryType
        )
        _uiState.update {
            it.copy(
                groupedCategories = null,
                groupedCategoriesByType = newCategoriesWithSubcategories
            )
        }
    }

    fun deleteCategory(category: Category) {
        if (!category.canBeDeleted()) return

        if (uiState.value.groupedCategories == null) {
            deleteParentCategory(category)
        } else {
            deleteSubcategoryById(category.id)
        }
    }

    private fun deleteParentCategory(category: Category) {
        _uiState.update {
            it.copy(
                groupedCategoriesByType = it.groupedCategoriesByType
                    .deleteCategoryById(category)
            )
        }
    }

    private fun deleteSubcategoryById(id: Int) {
        _uiState.update {
            it.copy(
                groupedCategories = it.groupedCategories?.deleteSubcategoryById(id)
            )
        }
    }

    fun saveEditedCategory(category: Category) {
        _uiState.update {
            it.saveCategory(category)
        }
    }


    suspend fun saveCategories() {
        val categories = uiState.value.groupedCategoriesByType
            .fixParentCategoriesOrderNums()
            .asList()
        saveCategoriesUseCase.saveAndDeleteRest(categories = categories)
    }

}