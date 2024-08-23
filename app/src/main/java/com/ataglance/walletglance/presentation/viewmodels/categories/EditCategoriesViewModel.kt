package com.ataglance.walletglance.presentation.viewmodels.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ataglance.walletglance.domain.categories.CategoriesWithSubcategories
import com.ataglance.walletglance.domain.categories.Category
import com.ataglance.walletglance.domain.categories.CategoryType
import com.ataglance.walletglance.domain.categories.CategoryWithSubcategories
import com.ataglance.walletglance.domain.categories.color.CategoryColors
import com.ataglance.walletglance.domain.categories.icons.CategoryIcon
import com.ataglance.walletglance.data.local.entities.CategoryEntity
import com.ataglance.walletglance.data.mappers.toCategoryEntityList
import com.ataglance.walletglance.domain.utils.toCategoryColorWithName
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
            .concatenateAsCategoryList().toCategoryEntityList()
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

    fun getCategoriesWithSubcategoriesListByType(): List<CategoryWithSubcategories> {
        return categoriesWithSubcategories.getByType(categoryType)
    }

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