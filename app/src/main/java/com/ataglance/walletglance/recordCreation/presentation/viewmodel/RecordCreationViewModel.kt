package com.ataglance.walletglance.recordCreation.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.category.domain.model.CategoryWithSub
import com.ataglance.walletglance.category.domain.model.CategoryWithSubByType
import com.ataglance.walletglance.category.domain.model.GroupedCategoriesByType
import com.ataglance.walletglance.category.domain.usecase.GetCategoriesUseCase
import com.ataglance.walletglance.category.domain.usecase.GetLastUsedRecordCategoryUseCase
import com.ataglance.walletglance.core.domain.date.DateTimeState
import com.ataglance.walletglance.core.utils.isNumberWithDecimalOptionalDot
import com.ataglance.walletglance.record.domain.usecase.GetLastRecordNumUseCase
import com.ataglance.walletglance.recordCreation.domain.usecase.DeleteRecordUseCase
import com.ataglance.walletglance.recordCreation.domain.usecase.GetRecordDraftUseCase
import com.ataglance.walletglance.recordCreation.domain.usecase.SaveRecordUseCase
import com.ataglance.walletglance.recordCreation.mapper.toCreatedRecord
import com.ataglance.walletglance.recordCreation.presentation.model.record.RecordDraft
import com.ataglance.walletglance.recordCreation.presentation.model.record.RecordDraftGeneral
import com.ataglance.walletglance.recordCreation.presentation.model.record.RecordDraftItem
import com.ataglance.walletglance.recordCreation.utils.copyWithCategoryAndSubcategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecordCreationViewModel(
    recordNum: Int?,
    private val getRecordDraftUseCase: GetRecordDraftUseCase,
    private val saveRecordUseCase: SaveRecordUseCase,
    private val deleteRecordUseCase: DeleteRecordUseCase,
    private val getLastUsedRecordCategoryUseCase: GetLastUsedRecordCategoryUseCase,
    private val getLastRecordNumUseCase: GetLastRecordNumUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    init {
        viewModelScope.launch {
            val categories = getCategoriesUseCase.getGrouped()
            _groupedCategoriesByType.update { categories }

            val category = getLastUsedRecordCategoryUseCase.get(CategoryType.Expense)

            defaultCategoryByType = defaultCategoryByType.putByType(
                type = CategoryType.Expense,
                categoryWithSub = category
            )

            val recordDraft = getRecordDraftUseCase.get(
                recordNum = recordNum, categoryWithSub = category
            )
            _recordDraftGeneral.update { recordDraft.general }
            _recordDraftItems.update { recordDraft.items }
        }
    }


    private val _groupedCategoriesByType = MutableStateFlow(GroupedCategoriesByType())
    val groupedCategoriesByType = _groupedCategoriesByType.asStateFlow()


    private var defaultCategoryByType = CategoryWithSubByType()

    private suspend fun getCategoryByType(type: CategoryType): CategoryWithSub? {
        return defaultCategoryByType.getByType(type = type) ?: let {
            defaultCategoryByType = defaultCategoryByType.putByType(
                type = type,
                categoryWithSub = getLastUsedRecordCategoryUseCase.get(type)
            )
            defaultCategoryByType.getByType(type = type)
        }
    }


    private val _recordDraftGeneral = MutableStateFlow(RecordDraftGeneral())
    val recordDraftGeneral = _recordDraftGeneral.asStateFlow()

    fun selectCategoryType(type: CategoryType) {
        _recordDraftGeneral.update {
            it.copy(type = type)
        }
        viewModelScope.launch {
            _recordDraftItems.update {
                it.copyWithCategoryAndSubcategory(getCategoryByType(type))
            }
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


    private val _recordDraftItems = MutableStateFlow<List<RecordDraftItem>>(emptyList())
    val recordDraftItems = _recordDraftItems.asStateFlow()

    fun changeAmount(index: Int, value: String) {
        val newList = recordDraftItems.value.toMutableList()
        if (index > newList.lastIndex) { return }

        val finalValue = value.takeIf { it.isNumberWithDecimalOptionalDot() } ?: return

        newList[index] = newList[index].copy(amount = finalValue)
        _recordDraftItems.update { newList }
    }

    fun selectCategory(index: Int, categoryWithSub: CategoryWithSub) {
        val newList = recordDraftItems.value.toMutableList()
        if (index > newList.lastIndex) { return }

        newList[index] = newList[index].copy(categoryWithSub = categoryWithSub)
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
                categoryWithSub = newList.lastOrNull()?.categoryWithSub,
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

        _recordDraftItems.update { newList }
    }

    fun deleteDraftItem(index: Int) {
        val itemList = recordDraftItems.value
        if (index > itemList.lastIndex) { return }

        val newItemList = itemList.subList(0, index).toMutableList()

        for (i in (index + 1)..itemList.lastIndex) {
            newItemList.add(itemList[i].copy(index = i - 1))
        }

        _recordDraftItems.update { newItemList }
    }


    val savingIsAllowed = combine(
        recordDraftGeneral, recordDraftItems
    ) { general, items ->
        general.savingIsAllowed() && items.all { it.savingIsAllowed() }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false
    )


    private fun getRecordDraft(): RecordDraft {
        return RecordDraft(
            general = recordDraftGeneral.value,
            items = recordDraftItems.value
        )
    }


    suspend fun saveRecord() {
        getRecordDraft()
            .takeIf { it.savingIsAllowed() }
            ?.toCreatedRecord()
            ?.let { createdRecord ->
                saveRecordUseCase.execute(record = createdRecord)
            }
    }

    suspend fun repeatRecord() {
        val recordDraft = getRecordDraft().takeIf { it.savingIsAllowed() } ?: return
        val recordNum = getLastRecordNumUseCase.getNext()

        val createdRecord = recordDraft
            .copy(
                general = recordDraft.general.copy(
                    isNew = true,
                    recordNum = recordNum,
                    dateTimeState = DateTimeState()
                )
            )
            .toCreatedRecord()
            ?: return

        saveRecordUseCase.execute(record = createdRecord)
    }

    suspend fun deleteRecord() {
        deleteRecordUseCase.execute(recordNum = recordDraftGeneral.value.recordNum)
    }

}