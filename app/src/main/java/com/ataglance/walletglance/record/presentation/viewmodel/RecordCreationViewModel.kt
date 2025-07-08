package com.ataglance.walletglance.record.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.usecase.GetAccountsUseCase
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.category.domain.model.CategoryWithSub
import com.ataglance.walletglance.category.domain.model.CategoryWithSubByType
import com.ataglance.walletglance.category.domain.model.GroupedCategoriesByType
import com.ataglance.walletglance.category.domain.usecase.GetCategoriesUseCase
import com.ataglance.walletglance.core.domain.date.DateTimeState
import com.ataglance.walletglance.core.utils.isNumberWithDecimalOptionalDot
import com.ataglance.walletglance.record.domain.usecase.DeleteRecordUseCase
import com.ataglance.walletglance.record.domain.usecase.GetLastUsedRecordCategoryUseCase
import com.ataglance.walletglance.record.domain.usecase.GetRecordDraftUseCase
import com.ataglance.walletglance.record.domain.usecase.SaveRecordUseCase
import com.ataglance.walletglance.record.mapper.toDomainModelWithItems
import com.ataglance.walletglance.record.presentation.model.RecordDraft
import com.ataglance.walletglance.record.presentation.model.RecordDraftItem
import com.ataglance.walletglance.record.presentation.model.RecordDraftWithItems
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecordCreationViewModel(
    recordId: Long?,
    accountId: Int?,
    private val deleteRecordUseCase: DeleteRecordUseCase,
    private val saveRecordUseCase: SaveRecordUseCase,
    private val getRecordDraftUseCase: GetRecordDraftUseCase,
    private val getLastUsedRecordCategoryUseCase: GetLastUsedRecordCategoryUseCase,
    private val getAccountsUseCase: GetAccountsUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    private val _accounts = MutableStateFlow<List<Account>>(emptyList())
    val accounts = _accounts.asStateFlow()


    private val _groupedCategoriesByType = MutableStateFlow(GroupedCategoriesByType())
    val groupedCategoriesByType = _groupedCategoriesByType.asStateFlow()


    private var defaultCategoryByType = CategoryWithSubByType()

    private suspend fun getCategoryByType(
        type: CategoryType,
        accountId: Int? = _recordDraft.value.account?.id,
        categories: GroupedCategoriesByType = _groupedCategoriesByType.value
    ): CategoryWithSub? {
        return defaultCategoryByType.getByType(type = type) ?: let {
            val categoryWithSub = getLastUsedRecordCategoryUseCase.execute(
                type = type, accountId = accountId, categories = categories
            )
            defaultCategoryByType = defaultCategoryByType.putByType(
                type = type, categoryWithSub = categoryWithSub
            )
            categoryWithSub
        }
    }


    private val _recordDraft = MutableStateFlow(RecordDraft())
    val recordDraft = _recordDraft.asStateFlow()

    fun selectCategoryType(type: CategoryType) {
        _recordDraft.update {
            it.copy(type = type)
        }
        viewModelScope.launch {
            _recordDraftItems.update { items ->
                val categoryWithSub = getCategoryByType(type = type)
                items.map { it.copy(categoryWithSub = categoryWithSub) }
            }
        }
    }

    fun selectDate(selectedDateMillis: Long) {
        _recordDraft.update {
            it.copy(dateTimeState = it.dateTimeState.applyNewDate(timestamp = selectedDateMillis))
        }
    }

    fun selectTime(hour: Int, minute: Int) {
        _recordDraft.update {
            it.copy(dateTimeState = it.dateTimeState.applyNewTime(hour = hour, minute = minute))
        }
    }

    fun toggleSelectedAccount() {
        val accounts = _accounts.value
        val currentAccount = _recordDraft.value.account ?: return

        val index = accounts.indexOfFirst { it.id == currentAccount.id }
        if (index == -1) return

        (accounts.getOrNull(index + 1) ?: accounts.getOrNull(index - 1))?.let { account ->
            selectAccount(account = account)
        }
    }

    fun selectAccount(account: Account) {
        _recordDraft.update {
            it.copy(account = account)
        }
    }

    fun changeIncludeInBudgets(value: Boolean) {
        _recordDraft.update {
            it.copy(
                preferences = it.preferences.copy(includeInBudgets = value)
            )
        }
    }


    private val _recordDraftItems = MutableStateFlow<List<RecordDraftItem>>(emptyList())
    val recordDraftItems = _recordDraftItems.asStateFlow()

    fun changeAmount(index: Int, value: String) {
        val newList = _recordDraftItems.value.toMutableList()
        if (index > newList.lastIndex) { return }

        val finalValue = value.takeIf { it.isNumberWithDecimalOptionalDot() } ?: return

        newList[index] = newList[index].copy(amount = finalValue)
        _recordDraftItems.update { newList }
    }

    fun selectCategory(index: Int, categoryWithSub: CategoryWithSub) {
        val newList = _recordDraftItems.value.toMutableList()
        if (index > newList.lastIndex) { return }

        newList[index] = newList[index].copy(categoryWithSub = categoryWithSub)
        _recordDraftItems.update { newList }
    }

    fun changeNote(index: Int, value: String) {
        val newList = _recordDraftItems.value.toMutableList()
        if (index > newList.lastIndex) { return }

        newList[index] = newList[index].copy(note = value)
        _recordDraftItems.update { newList }
    }

    fun changeQuantity(index: Int, value: String) {
        val newList = _recordDraftItems.value.toMutableList()
        if (index > newList.lastIndex) { return }

        val finalValue = value.takeIf {
            Regex("^\\d*\$").matches(it)
        } ?: return

        newList[index] = newList[index].copy(quantity = finalValue)
        _recordDraftItems.update { newList }
    }

    fun changeCollapsed(index: Int, collapsed: Boolean) {
        val newList = _recordDraftItems.value.toMutableList()
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
        val items = _recordDraftItems.value
            .map { it.copy(collapsed = true) }
            .toMutableList()
            .apply {
                add(
                    element = RecordDraftItem(
                        lazyListKey = maxOfOrNull { it.lazyListKey }?.plus(1) ?: 0,
                        categoryWithSub = lastOrNull()?.categoryWithSub,
                        collapsed = false
                    )
                )
            }

        _recordDraftItems.update { items }
    }

    fun swapDraftItems(firstIndex: Int, secondIndex: Int) {
        val items = _recordDraftItems.value
        val lastIndex = items.lastIndex
        if (firstIndex !in 0..lastIndex || secondIndex !in 0..lastIndex) return

        val firstItem = items[firstIndex]
        val secondItem = items[secondIndex]

        val newItems = items.toMutableList()
        newItems[firstIndex] = secondItem
        newItems[secondIndex] = firstItem

        _recordDraftItems.update { newItems }
    }

    fun deleteDraftItem(index: Int) {
        val items = _recordDraftItems.value
        if (index !in 0..items.lastIndex) return

        val newItems = items.toMutableList().apply { removeAt(index) }

        _recordDraftItems.update { newItems }
    }


    val savingIsAllowed = combine(
        _recordDraft, _recordDraftItems
    ) { general, items ->
        general.savingIsAllowed && items.all { it.savingIsAllowed }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )


    private fun getRecordDraftWithItems(): RecordDraftWithItems {
        return RecordDraftWithItems(
            record = _recordDraft.value,
            items = _recordDraftItems.value
        )
    }


    suspend fun saveRecord() {
        val createdRecord = getRecordDraftWithItems()
            .takeIf { it.savingIsAllowed }
            ?.toDomainModelWithItems()
            ?: return

        saveRecordUseCase.execute(createdRecord = createdRecord)
    }

    suspend fun repeatRecord() {
        val recordDraftWithItems = getRecordDraftWithItems().takeIf { it.savingIsAllowed } ?: return

        val createdRecord = recordDraftWithItems
            .copy(
                record = recordDraftWithItems.record.copy(
                    recordId = 0,
                    dateTimeState = DateTimeState.fromCurrentTime()
                )
            )
            .toDomainModelWithItems()
            ?: return

        saveRecordUseCase.execute(createdRecord = createdRecord)
    }

    suspend fun deleteRecord() {
        val id = _recordDraft.value.recordId.takeUnless { it == 0L } ?: return

        deleteRecordUseCase.execute(id = id)
    }


    init {
        viewModelScope.launch {
            val accounts = getAccountsUseCase.getAll()
            _accounts.update { accounts }

            val categories = getCategoriesUseCase.getGrouped()
            _groupedCategoriesByType.update { categories }

            val categoryWithSub = getLastUsedRecordCategoryUseCase.execute(
                type = CategoryType.Expense, accountId = accountId, categories = categories
            )

            defaultCategoryByType = defaultCategoryByType.putByType(
                type = CategoryType.Expense, categoryWithSub = categoryWithSub
            )

            val recordDraftWithItems = getRecordDraftUseCase.execute(
                id = recordId,
                accountId = accountId,
                accounts = accounts,
                categoryWithSub = categoryWithSub,
                categories = categories
            )
            _recordDraft.update { recordDraftWithItems.record }
            _recordDraftItems.update { recordDraftWithItems.items }
        }
    }

}