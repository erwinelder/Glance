package com.ataglance.walletglance.budget.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.budget.domain.model.BudgetsByType
import com.ataglance.walletglance.budget.domain.usecase.GetBudgetIdsOnWidgetUseCase
import com.ataglance.walletglance.budget.domain.usecase.GetBudgetsUseCase
import com.ataglance.walletglance.budget.domain.usecase.SaveBudgetsOnWidgetUseCase
import com.ataglance.walletglance.budget.mapper.budget.toCheckedBudgetsByType
import com.ataglance.walletglance.budget.presentation.model.CheckedBudgetsByType
import com.ataglance.walletglance.personalization.domain.model.WidgetName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BudgetsOnWidgetSettingsViewModel(
    private val saveBudgetsOnWidgetUseCase: SaveBudgetsOnWidgetUseCase,
    private val getBudgetIdsOnWidgetUseCase: GetBudgetIdsOnWidgetUseCase,
    private val getBudgetsUseCase: GetBudgetsUseCase
) : ViewModel() {

    init {
        viewModelScope.launch {
            getBudgetsUseCase.getGroupedByTypeFlow().collect { budgetsByType ->
                _budgetsByType.update { budgetsByType }
            }
        }
        viewModelScope.launch {
            getBudgetIdsOnWidgetUseCase.getAsFlow().collect { budgetsIds ->
                _budgetsOnWidget.update { budgetsIds }
            }
        }
    }


    private val _openedWidgetSettings: MutableStateFlow<WidgetName?> = MutableStateFlow(null)
    val openedWidgetSettings = _openedWidgetSettings.asStateFlow()

    fun openWidgetSettings(widgetName: WidgetName) {
        _openedWidgetSettings.update { widgetName }
    }

    fun closeWidgetSettings() {
        _openedWidgetSettings.update { null }
    }


    private val _budgetsByType = MutableStateFlow(BudgetsByType())

    private val _budgetsOnWidget = MutableStateFlow<List<Int>>(emptyList())

    fun checkBudgetOnWidget(budgetId: Int) {
        _budgetsOnWidget.update {
            it.toMutableList().apply { add(budgetId) }
        }
    }

    fun uncheckBudgetOnWidget(budgetId: Int) {
        _budgetsOnWidget.update {
            it.toMutableList().apply { remove(budgetId) }
        }
    }


    val checkedBudgetsByType = combine(
        _budgetsByType,
        _budgetsOnWidget
    ) { budgetsByType, budgetsIds ->
        budgetsByType.toCheckedBudgetsByType(budgetsIds)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = CheckedBudgetsByType()
    )

    val checkedBudgetsLimitIsReached = combine(checkedBudgetsByType) { checkedBudgetsByType ->
        checkedBudgetsByType[0].countCheckedBudgets() >= 3
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = true
    )


    fun saveCurrentBudgetsOnWidget() {
        viewModelScope.launch {
            saveBudgetsOnWidgetUseCase.execute(budgetsIds = _budgetsOnWidget.value)
        }
    }

}