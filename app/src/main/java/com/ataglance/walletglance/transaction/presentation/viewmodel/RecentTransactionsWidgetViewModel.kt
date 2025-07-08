package com.ataglance.walletglance.transaction.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.usecase.GetAccountsUseCase
import com.ataglance.walletglance.category.domain.model.GroupedCategoriesByType
import com.ataglance.walletglance.category.domain.usecase.GetCategoriesUseCase
import com.ataglance.walletglance.core.domain.date.TimestampRange
import com.ataglance.walletglance.core.presentation.model.ResourceManager
import com.ataglance.walletglance.transaction.domain.usecase.GetTransactionsInDateRangeUseCase
import com.ataglance.walletglance.transaction.domain.utils.filterByAccount
import com.ataglance.walletglance.transaction.mapper.toUiStates
import com.ataglance.walletglance.transaction.presentation.model.RecentTransactionsWidgetUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class RecentTransactionsWidgetViewModel(
    activeAccount: Account?,
    activeDateRange: TimestampRange,
    private val resourceManager: ResourceManager,
    private val getAccountsUseCase: GetAccountsUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getTransactionsInDateRangeUseCase: GetTransactionsInDateRangeUseCase
) : ViewModel() {

    private var accounts: List<Account>? = null
    private var groupedCategoriesByType: GroupedCategoriesByType? = null


    private val _activeAccountId = MutableStateFlow(activeAccount?.id)

    fun setActiveAccountId(id: Int) {
        if (_activeAccountId.value == id) return
        _activeAccountId.update { id }
    }


    private val _activeDateRange = MutableStateFlow(activeDateRange)

    fun setActiveDateRange(dateRange: TimestampRange) {
        if (_activeDateRange.value.equalsTo(dateRange)) return
        _activeDateRange.update { dateRange }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    private val _transactionsInDateRange = _activeDateRange.flatMapLatest { dateRange ->
        getTransactionsInDateRangeUseCase.getAsFlow(range = dateRange)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5000),
        initialValue = emptyList()
    )


    val uiState: StateFlow<RecentTransactionsWidgetUiState> = combine(
        _transactionsInDateRange,
        _activeAccountId
    ) { transactions, accountId ->
        if (accountId == null) return@combine RecentTransactionsWidgetUiState()

        val accounts = accounts ?: getAccountsUseCase.getAll().also { accounts = it }
        val groupedCategoriesByType = groupedCategoriesByType
            ?: getCategoriesUseCase.getGrouped().also { groupedCategoriesByType = it }

        transactions
            .filterByAccount(accountId = accountId)
            .sortedByDescending { it.date }
            .toUiStates(
                accountId = accountId,
                accounts = accounts,
                groupedCategoriesByType = groupedCategoriesByType,
                resourceManager = resourceManager
            )
            .let {
                RecentTransactionsWidgetUiState(
                    firstRecord = it.getOrNull(0),
                    secondRecord = it.getOrNull(1),
                    thirdRecord = it.getOrNull(2)
                )
            }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(),
        initialValue = RecentTransactionsWidgetUiState()
    )

}