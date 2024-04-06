package com.ataglance.walletglance.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ataglance.walletglance.data.Account
import com.ataglance.walletglance.data.Category
import com.ataglance.walletglance.data.Record
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Locale

class MakeRecordViewModel(
    category: Category?,
    subcategory: Category?,
    makeRecordUiState: MakeRecordUiState,
    makeRecordUnitList: List<MakeRecordUnitUiState>?,
) : ViewModel() {

    private val _uiState: MutableStateFlow<MakeRecordUiState> = MutableStateFlow(makeRecordUiState)
    val uiState: StateFlow<MakeRecordUiState> = _uiState.asStateFlow()

    fun selectNewDate(selectedDateMillis: Long) {
        _uiState.update { it.copy(
            dateTimeState = DateTimeController().getNewDate(
                selectedDateMillis = selectedDateMillis,
                dateTimeState = uiState.value.dateTimeState
            )
        ) }
    }

    fun selectNewTime(hour: Int, minute: Int) {
        _uiState.update { it.copy(
            dateTimeState = DateTimeController().getNewTime(
                dateTimeState = uiState.value.dateTimeState,
                hour = hour,
                minute = minute
            )
        ) }
    }

    fun changeRecordType(type: RecordType, categoriesUiState: CategoriesUiState) {
        _uiState.update { it.copy(type = type) }
        changeAllUnitCategoriesByType(type, categoriesUiState)
    }


    private val _recordUnitList: MutableStateFlow<List<MakeRecordUnitUiState>> = MutableStateFlow(
        makeRecordUnitList ?: listOf(
                MakeRecordUnitUiState(
                    index = 0,
                    category = category,
                    subcategory = subcategory,
                    note = ""
                )
            )
    )
    val recordUnitList: StateFlow<List<MakeRecordUnitUiState>> = _recordUnitList.asStateFlow()

    private fun changeAllUnitCategoriesByType(type: RecordType, categoriesUiState: CategoriesUiState) {
        val parentCategory = if (type == RecordType.Expense) {
            categoriesUiState.parentCategories.expense.last()
        } else {
            categoriesUiState.parentCategories.income.last()
        }

        val subcategory = if (parentCategory.parentCategoryId != null) {
            if (type == RecordType.Expense) {
                categoriesUiState.subcategories.expense.last().last()
            } else {
                categoriesUiState.subcategories.income.last().last()
            }
        } else {
            null
        }

        val newList = mutableListOf<MakeRecordUnitUiState>()
        recordUnitList.value.forEach { unit ->
            newList.add(unit.copy(category = parentCategory, subcategory = subcategory))
        }
        _recordUnitList.update { newList }
    }

    fun chooseAccount(account: Account) {
        _uiState.update { it.copy(account = account) }
    }

    fun changeNoteValue(index: Int, value: String) {
        val newList = recordUnitList.value.toMutableList()
        newList[index] = newList[index].copy(note = value)
        _recordUnitList.update { newList }
    }

    fun changeClickedUnitIndex(index: Int) {
        _uiState.update { it.copy(clickedUnitIndex = index) }
    }

    fun chooseCategory(category: Category, subcategory: Category?) {
        val newList = recordUnitList.value.toMutableList()
        newList[uiState.value.clickedUnitIndex] = newList[uiState.value.clickedUnitIndex].copy(
            category = category, subcategory = subcategory
        )
        _recordUnitList.update { newList }
    }

    fun changeAmountValue(index: Int, value: String) {
        val newList = recordUnitList.value.toMutableList()
        val newValue = value.takeIf {
            Regex("^(?:[0-9]\\d{0,9}(?:[.]\\d{0,2})?)?\$").matches(it)
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

    fun addNewRecordUnit() {
        val newList = recordUnitList.value.toMutableList()
        newList.add(
            MakeRecordUnitUiState(
                index = newList.last().index + 1,
                category = newList.last().category,
                subcategory = newList.last().subcategory
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
    private val category: Category?,
    private val subcategory: Category?,
    private val makeRecordUiState: MakeRecordUiState,
    private val makeRecordUnitList: List<MakeRecordUnitUiState>?,
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MakeRecordViewModel(
            category = category,
            subcategory = subcategory,
            makeRecordUiState = makeRecordUiState,
            makeRecordUnitList = makeRecordUnitList,
        ) as T
    }
}

data class MakeRecordUiState(
    val recordStatus: MakeRecordStatus,
    val recordNum: Int?,
    val account: Account?,
    val type: RecordType = RecordType.Expense,
    val clickedUnitIndex: Int = 0,
    val dateTimeState: DateTimeState = DateTimeState()
) {

    fun toRecordList(
        unitList: List<MakeRecordUnitUiState>,
        lastRecordNum: Int
    ): List<Record> {
        val recordList = mutableListOf<Record>()

        unitList.forEach { unit ->
            if (account != null && unit.category != null) {
                recordList.add(
                    Record(
                        recordNum = recordNum ?: (lastRecordNum + 1),
                        date = dateTimeState.dateLong,
                        type = if (type == RecordType.Expense) '-' else '+',
                        amount = if (unit.quantity.isNotBlank()) {
                            "%.2f".format(
                                Locale.US,
                                unit.amount.toDouble() * unit.quantity.toInt()
                            ).toDouble()
                        } else {
                            unit.amount.toDouble()
                        },
                        quantity = unit.quantity.ifBlank { null }?.toInt(),
                        categoryId = unit.category.id,
                        subcategoryId = unit.subcategory?.id,
                        accountId = account.id,
                        note = unit.note.ifBlank { null }
                    )
                )
            }
        }

        return recordList
    }

    fun toRecordListWithOldIds(
        unitList: List<MakeRecordUnitUiState>,
        recordStack: RecordStack
    ): List<Record> {
        val recordList = mutableListOf<Record>()

        unitList.forEach { unit ->
            if (account != null && unit.category != null) {
                recordList.add(
                    Record(
                        id = recordStack.stack[unit.index].id,
                        recordNum = recordStack.recordNum,
                        date = dateTimeState.dateLong,
                        type = if (type == RecordType.Expense) '-' else '+',
                        amount = if (unit.quantity.isNotBlank()) {
                            "%.2f".format(
                                Locale.US,
                                unit.amount.toDouble() * unit.quantity.toInt()
                            ).toDouble()
                        } else {
                            unit.amount.toDouble()
                        },
                        quantity = unit.quantity.ifBlank { null }?.toInt(),
                        categoryId = unit.category.id,
                        subcategoryId = unit.subcategory?.id,
                        accountId = account.id,
                        note = unit.note.ifBlank { null }
                    )
                )
            }
        }

        return recordList
    }

}

data class MakeRecordUnitUiState(
    val index: Int,
    val category: Category?,
    val subcategory: Category?,
    val note: String = "",
    val amount: String = "",
    val quantity: String = ""
)