package com.ataglance.walletglance.category.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.category.domain.Category
import com.ataglance.walletglance.category.domain.color.CategoryPossibleColors
import com.ataglance.walletglance.category.domain.icons.CategoryIcon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class EditCategoryViewModel : ViewModel() {

    private val _categoryUiState: MutableStateFlow<Category> =
        MutableStateFlow(Category())
    val categoryUiState: StateFlow<Category> = _categoryUiState.asStateFlow()

    val allowSaving: StateFlow<Boolean> = combine(_categoryUiState) { categoryUiStateArray ->
        categoryUiStateArray[0].name.isNotBlank()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false
    )

    val allowDeleting: StateFlow<Boolean> = combine(_categoryUiState) { categoryUiStateArray ->
        categoryUiStateArray[0].canBeDeleted() && categoryUiStateArray[0].orderNum != 0
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false
    )


    fun applyCategory(category: Category) {
        _categoryUiState.update { category }
    }

    fun changeName(value: String) {
        _categoryUiState.update {
            it.copy(name = value)
        }
    }

    fun changeIcon(icon: CategoryIcon) {
        _categoryUiState.update {
            it.copy(icon = icon)
        }
    }

    fun changeColor(colorName: String) {
        _categoryUiState.update {
            it.copy(colorWithName = CategoryPossibleColors().getByName(colorName))
        }
    }

    fun getCategory(): Category {
        return categoryUiState.value.copy(
            name = categoryUiState.value.name.trim()
        )
    }

}