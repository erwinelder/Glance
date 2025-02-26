package com.ataglance.walletglance.record.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.core.domain.date.LongDateRange
import com.ataglance.walletglance.record.domain.usecase.GetRecordStacksInDateRangeUseCase
import com.ataglance.walletglance.record.domain.utils.filterByAccount
import com.ataglance.walletglance.record.domain.utils.shrinkForCompactView
import com.ataglance.walletglance.record.presentation.model.RecentRecordsWidgetUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class RecentRecordsWidgetViewModel(
    activeAccount: Account?,
    activeDateRange: LongDateRange,
    private val getRecordStacksInDateRangeUseCase: GetRecordStacksInDateRangeUseCase
) : ViewModel() {

    private val _activeAccountId = MutableStateFlow(activeAccount?.id)

    fun setActiveAccountId(id: Int) {
        if (_activeAccountId.value == id) return
        _activeAccountId.update { id }
    }


    private val _activeDateRange = MutableStateFlow(activeDateRange)

    fun setActiveDateRange(dateRange: LongDateRange) {
        if (_activeDateRange.value.equalsTo(dateRange)) return
        _activeDateRange.update { dateRange }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    private val _recordsInDateRange = _activeDateRange.flatMapLatest { dateRange ->
        getRecordStacksInDateRangeUseCase.getFlow(range = dateRange)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )


    val uiState: StateFlow<RecentRecordsWidgetUiState> = combine(
        _recordsInDateRange,
        _activeAccountId
    ) { stacks, accountId ->
        stacks.filterByAccount(accountId).shrinkForCompactView().let {
            RecentRecordsWidgetUiState(
                firstRecord = it.getOrNull(0),
                secondRecord = it.getOrNull(1),
                thirdRecord = it.getOrNull(2)
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = RecentRecordsWidgetUiState()
    )

}