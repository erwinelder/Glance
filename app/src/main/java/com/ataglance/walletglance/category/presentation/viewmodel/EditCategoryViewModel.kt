package com.ataglance.walletglance.category.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.category.domain.model.CategoryColor
import com.ataglance.walletglance.category.domain.model.CategoryIcon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class EditCategoryViewModel : ViewModel() {

    private val _category = MutableStateFlow(Category())
    val category = _category.asStateFlow()

    val allowSaving: StateFlow<Boolean> = combine(_category) { category ->
        category[0].savingIsAllowed()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false
    )

    val allowDeleting: StateFlow<Boolean> = combine(_category) { category ->
        category[0].canBeDeleted() && category[0].orderNum != 0
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false
    )


    fun applyCategory(category: Category) {
        _category.update { category }
    }

    fun changeName(value: String) {
        _category.update {
            it.copy(name = value)
        }
    }

    fun changeIcon(icon: CategoryIcon) {
        _category.update {
            it.copy(icon = icon)
        }
    }

    fun changeColor(colorName: String) {
        _category.update {
            it.copy(color = CategoryColor.getByName(colorName))
        }
    }

    fun getCategory(): Category {
        return _category.value.let { it.copy(name = it.name.trim()) }
    }

}