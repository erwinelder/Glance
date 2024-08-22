package com.ataglance.walletglance.presentation.viewmodels.records

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.data.accounts.Account
import com.ataglance.walletglance.data.categories.CategoriesWithSubcategories
import com.ataglance.walletglance.data.categories.CategoryWithSubcategory
import com.ataglance.walletglance.data.makingRecord.MakeRecordUiState
import com.ataglance.walletglance.data.makingRecord.MakeRecordUnitUiState
import com.ataglance.walletglance.data.records.RecordType
import com.ataglance.walletglance.data.utils.addZeroIfDotIsAtTheBeginning
import com.ataglance.walletglance.data.utils.copyWithCategoryWithSubcategory
import com.ataglance.walletglance.data.utils.toCategoryType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class MakeRecordViewModel(
    categoryWithSubcategory: CategoryWithSubcategory?,
    makeRecordUiState: MakeRecordUiState,
    makeRecordUnitList: List<MakeRecordUnitUiState>?,
) : ViewModel() {

    private val _uiState: MutableStateFlow<MakeRecordUiState> = MutableStateFlow(makeRecordUiState)
    val uiState: StateFlow<MakeRecordUiState> = _uiState.asStateFlow()

    fun selectNewDate(selectedDateMillis: Long) {
        _uiState.update {
            it.copy(
                dateTimeState = uiState.value.dateTimeState.getNewDate(selectedDateMillis)
            )
        }
    }

    fun selectNewTime(hour: Int, minute: Int) {
        _uiState.update {
            it.copy(
                dateTimeState = uiState.value.dateTimeState.getNewTime(hour, minute)
            )
        }
    }

    fun changeRecordType(
        type: RecordType,
        categoriesWithSubcategories: CategoriesWithSubcategories
    ) {
        _uiState.update { it.copy(type = type) }
        changeAllUnitCategoriesByType(type, categoriesWithSubcategories)
    }


    private val _recordUnitList: MutableStateFlow<List<MakeRecordUnitUiState>> = MutableStateFlow(
        makeRecordUnitList ?: listOf(
                MakeRecordUnitUiState(
                    lazyListKey = 0,
                    index = 0,
                    categoryWithSubcategory = categoryWithSubcategory,
                    note = "",
                    collapsed = false
                )
            )
    )
    val recordUnitList: StateFlow<List<MakeRecordUnitUiState>> = _recordUnitList.asStateFlow()


    val allowSaving = combine(
        _recordUnitList, _uiState
    ) { recordUnitList, uiState ->
        recordUnitList.none { recordUnit ->
            recordUnit.amount.isBlank() ||
                    recordUnit.amount.last() == '.' ||
                    recordUnit.amount.addZeroIfDotIsAtTheBeginning().toDouble() == 0.0 ||
                    recordUnit.categoryWithSubcategory == null
        } && uiState.account != null
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false
    )


    private fun changeAllUnitCategoriesByType(
        type: RecordType,
        categoriesWithSubcategories: CategoriesWithSubcategories
    ) {
        val categoryWithSubcategory = categoriesWithSubcategories
            .getLastCategoryWithSubcategoryByType(type.toCategoryType())
        _recordUnitList.update {
            recordUnitList.value.copyWithCategoryWithSubcategory(categoryWithSubcategory)
        }
    }

    fun selectAccount(account: Account) {
        _uiState.update { it.copy(account = account) }
    }

    fun toggleSelectedAccount(currentAccount: Account, accountList: List<Account>) {
        if (accountList.size < 2) return

        for (i in accountList.indices) {
            if (accountList[i].id == currentAccount.id ) {
                (accountList.getOrNull(i + 1) ?: accountList.getOrNull(i - 1))
                    ?.let { account ->
                        _uiState.update { it.copy(account = account) }
                    } ?: return
                break
            }
        }
    }

    fun changeNoteValue(index: Int, value: String) {
        val newList = recordUnitList.value.toMutableList()
        newList[index] = newList[index].copy(note = value)
        _recordUnitList.update { newList }
    }

    fun changeClickedUnitIndex(index: Int) {
        _uiState.update { it.copy(clickedUnitIndex = index) }
    }

    fun chooseCategory(categoryWithSubcategory: CategoryWithSubcategory) {
        val newList = recordUnitList.value.toMutableList()
        newList[uiState.value.clickedUnitIndex] = newList[uiState.value.clickedUnitIndex].copy(
            categoryWithSubcategory = categoryWithSubcategory
        )
        _recordUnitList.update { newList }
    }

    fun changeAmountValue(index: Int, value: String) {
        val newList = recordUnitList.value.toMutableList()
        val newValue = value.takeIf {
            Regex("^,?(?:\\d{1,10}(?:\\.\\d{0,2})?|\\.(?:\\d{1,2})?)?\$").matches(it)
        } ?: return
        newList[index] = newList[index].copy(amount = newValue)
        _recordUnitList.update { newList }
    }

    fun changeQuantityValue(index: Int, value: String) {
        val newList = recordUnitList.value.toMutableList()
        val newValue = value.takeIf {
            Regex("^\\d*\$").matches(it)
        } ?: return
        newList[index] = newList[index].copy(quantity = newValue)
        _recordUnitList.update { newList }
    }

    fun changeCollapsedValue(index: Int, collapsed: Boolean) {
        val newList = recordUnitList.value.toMutableList()

        if (!collapsed) {
            for (i in 0..newList.lastIndex) {
                if (i == index) {
                    newList[i] = newList[i].copy(collapsed = false)
                } else {
                    newList[i] = newList[i].copy(collapsed = true)
                }
            }
        } else {
            newList[index] = newList[index].copy(collapsed = true)
        }

        _recordUnitList.update { newList }
    }

    fun addNewRecordUnit() {
        val newList = recordUnitList.value.toMutableList()
        for (i in 0..newList.lastIndex) {
            newList[i] = newList[i].copy(collapsed = true)
        }
        newList.add(
            MakeRecordUnitUiState(
                lazyListKey = newList.maxOfOrNull { it.lazyListKey }?.let { it + 1 } ?: 0,
                index = newList.lastOrNull()?.let { it.index + 1 } ?: 0,
                categoryWithSubcategory = newList.lastOrNull()?.categoryWithSubcategory,
                collapsed = false
            )
        )
        _recordUnitList.update { newList }
    }

    fun swapRecordUnits(firstIndex: Int, secondIndex: Int) {
        if (firstIndex !in 0..<recordUnitList.value.size || secondIndex !in 0..<recordUnitList.value.size) {
            return
        }

        val firstUnit = recordUnitList.value[firstIndex]
        val secondUnit = recordUnitList.value[secondIndex]

        val newList = mutableListOf<MakeRecordUnitUiState>()

        recordUnitList.value.forEach { unit ->
            if (unit.index != firstUnit.index && unit.index != secondUnit.index) {
                newList.add(unit)
            } else if (unit.index == firstUnit.index) {
                newList.add(secondUnit.copy(index = unit.index))
            } else {
                newList.add(firstUnit.copy(index = unit.index))
            }
        }

        _recordUnitList.update { newList }
    }

    fun deleteRecordUnit(index: Int) {
        if (recordUnitList.value.isEmpty()) {
            return
        }

        val newList = mutableListOf<MakeRecordUnitUiState>()
        var stepAfterDeleting = 0

        recordUnitList.value.forEach { unit ->
            if (unit.index != index) {
                newList.add(unit.copy(index = unit.index - stepAfterDeleting))
            } else {
                stepAfterDeleting = 1
            }
        }

        _recordUnitList.update { newList }
    }

}

class MakeRecordViewModelFactory(
    private val categoryWithSubcategory: CategoryWithSubcategory?,
    private val makeRecordUiState: MakeRecordUiState,
    private val makeRecordUnitList: List<MakeRecordUnitUiState>?,
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MakeRecordViewModel(
            categoryWithSubcategory = categoryWithSubcategory,
            makeRecordUiState = makeRecordUiState,
            makeRecordUnitList = makeRecordUnitList,
        ) as T
    }
}