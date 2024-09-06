package com.ataglance.walletglance.recordCreation.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.account.domain.Account
import com.ataglance.walletglance.category.domain.CategoryType
import com.ataglance.walletglance.category.domain.CategoryWithSubcategory
import com.ataglance.walletglance.category.domain.CategoryWithSubcategoryByType
import com.ataglance.walletglance.core.utils.isNumberWithDecimalOptionalDot
import com.ataglance.walletglance.recordCreation.domain.record.RecordDraft
import com.ataglance.walletglance.recordCreation.domain.record.RecordDraftGeneral
import com.ataglance.walletglance.recordCreation.domain.record.RecordDraftItem
import com.ataglance.walletglance.recordCreation.utils.copyWithCategoryAndSubcategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class RecordCreationViewModel(
    private val initialCategoryWithSubcategoryByType: CategoryWithSubcategoryByType,
    recordDraft: RecordDraft
) : ViewModel() {

    private val _recordDraftGeneral: MutableStateFlow<RecordDraftGeneral> = MutableStateFlow(
        recordDraft.general
    )
    val recordDraftGeneral = _recordDraftGeneral.asStateFlow()

    fun selectCategoryType(type: CategoryType) {
        _recordDraftGeneral.update {
            it.copy(type = type)
        }
        _recordDraftItems.update {
            it.copyWithCategoryAndSubcategory(initialCategoryWithSubcategoryByType.getByType(type))
        }
    }

    fun changeIncludeInBudgets(value: Boolean) {
        _recordDraftGeneral.update {
            it.copy(
                preferences = it.preferences.copy(includeInBudgets = value)
            )
        }
    }

    fun selectDate(selectedDateMillis: Long) {
        _recordDraftGeneral.update {
            it.copy(dateTimeState = it.dateTimeState.getNewDate(selectedDateMillis))
        }
    }

    fun selectTime(hour: Int, minute: Int) {
        _recordDraftGeneral.update {
            it.copy(dateTimeState = it.dateTimeState.getNewTime(hour, minute))
        }
    }

    fun toggleSelectedAccount(accountList: List<Account>) {
        val currentAccount = recordDraftGeneral.value.account ?: return

        val index = accountList.indexOfFirst { it.id == currentAccount.id }
        if (index == -1) return

        (accountList.getOrNull(index + 1) ?: accountList.getOrNull(index - 1))?.let { account ->
            selectAccount(account)
        }
    }

    fun selectAccount(account: Account) {
        _recordDraftGeneral.update {
            it.copy(account = account)
        }
    }


    private val _recordDraftItems: MutableStateFlow<List<RecordDraftItem>> = MutableStateFlow(
        recordDraft.items
    )
    val recordDraftItems = _recordDraftItems.asStateFlow()

    fun changeAmount(index: Int, value: String) {
        val newList = recordDraftItems.value.toMutableList()
        if (index > newList.lastIndex) { return }

        val finalValue = value.takeIf { it.isNumberWithDecimalOptionalDot() } ?: return

        newList[index] = newList[index].copy(amount = finalValue)
        _recordDraftItems.update { newList }
    }

    fun selectCategory(index: Int, categoryWithSubcategory: CategoryWithSubcategory) {
        val newList = recordDraftItems.value.toMutableList()
        if (index > newList.lastIndex) { return }

        newList[index] = newList[index].copy(categoryWithSubcategory = categoryWithSubcategory)
        _recordDraftItems.update { newList }
    }

    fun changeNote(index: Int, value: String) {
        val newList = recordDraftItems.value.toMutableList()
        if (index > newList.lastIndex) { return }

        newList[index] = newList[index].copy(note = value)
        _recordDraftItems.update { newList }
    }

    fun changeQuantity(index: Int, value: String) {
        val newList = recordDraftItems.value.toMutableList()
        if (index > newList.lastIndex) { return }

        val finalValue = value.takeIf {
            Regex("^\\d*\$").matches(it)
        } ?: return

        newList[index] = newList[index].copy(quantity = finalValue)
        _recordDraftItems.update { newList }
    }

    fun changeCollapsed(index: Int, collapsed: Boolean) {
        val newList = recordDraftItems.value.toMutableList()
        if (index > newList.lastIndex) { return }

        if (!collapsed) {
            for (i in newList.indices) {
                if (i == index) {
                    newList[i] = newList[i].copy(collapsed = false)
                } else {
                    newList[i] = newList[i].copy(collapsed = true)
                }
            }
        } else {
            newList[index] = newList[index].copy(collapsed = true)
        }

        _recordDraftItems.update { newList }
    }

    fun addNewDraftItem() {
        val newList = recordDraftItems.value
            .map { it.copy(collapsed = true) }
            .toMutableList()

        newList.add(
            RecordDraftItem(
                lazyListKey = newList.maxOfOrNull { it.lazyListKey }?.plus(1) ?: 0,
                index = newList.lastIndex + 1,
                categoryWithSubcategory = newList.lastOrNull()?.categoryWithSubcategory,
                collapsed = false
            )
        )
        _recordDraftItems.update { newList }
    }

    fun swapDraftItems(firstIndex: Int, secondIndex: Int) {
        val lastIdx = recordDraftItems.value.lastIndex
        if (firstIndex < 0 || firstIndex > lastIdx || secondIndex < 0 || secondIndex > lastIdx) {
            return
        }

        val firstItem = recordDraftItems.value[firstIndex]
        val secondItem = recordDraftItems.value[secondIndex]

        val newList = recordDraftItems.value.toMutableList()
        newList[firstIndex] = secondItem.copy(index = firstIndex)
        newList[secondIndex] = firstItem.copy(index = secondIndex)

        /*recordDraftItems.value.forEach { item ->
            if (item.index != firstItem.index && item.index != secondItem.index) {
                newList.add(item)
            } else if (item.index == firstItem.index) {
                newList.add(secondItem.copy(index = item.index))
            } else {
                newList.add(firstItem.copy(index = item.index))
            }
        }*/

        _recordDraftItems.update { newList }
    }

    fun deleteDraftItem(index: Int) {
        val itemList = recordDraftItems.value
        if (index > itemList.lastIndex) { return }

        val newItemList = itemList.subList(0, index).toMutableList()

        for (i in (index + 1)..itemList.lastIndex) {
            newItemList.add(itemList[i].copy(index = i - 1))
        }

        _recordDraftItems.update { itemList }
    }


    val savingIsAllowed = combine(
        _recordDraftGeneral, _recordDraftItems
    ) { general, items ->
        general.savingIsAllowed() && items.all { it.savingIsAllowed() }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false
    )


    fun getRecordDraft(): RecordDraft {
        return RecordDraft(
            general = recordDraftGeneral.value,
            items = recordDraftItems.value
        )
    }

}

class RecordCreationViewModelFactory(
    private val initialCategoryWithSubcategoryByType: CategoryWithSubcategoryByType,
    private val recordDraft: RecordDraft
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RecordCreationViewModel(initialCategoryWithSubcategoryByType, recordDraft) as T
    }
}
