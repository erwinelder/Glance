package com.ataglance.walletglance.personalization.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.personalization.domain.model.CheckedWidget
import com.ataglance.walletglance.personalization.domain.model.WidgetName
import com.ataglance.walletglance.personalization.domain.usecase.GetBudgetIdsOnWidgetUseCase
import com.ataglance.walletglance.personalization.domain.usecase.GetWidgetsUseCase
import com.ataglance.walletglance.personalization.domain.usecase.SaveBudgetsOnWidgetUseCase
import com.ataglance.walletglance.personalization.domain.usecase.SaveWidgetsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PersonalizationViewModel(
    private val saveWidgetsUseCase: SaveWidgetsUseCase,
    private val getWidgetsUseCase: GetWidgetsUseCase,
    private val saveBudgetsOnWidgetUseCase: SaveBudgetsOnWidgetUseCase,
    private val getBudgetIdsOnWidgetUseCase: GetBudgetIdsOnWidgetUseCase
) : ViewModel() {

    private val _widgetNames: MutableStateFlow<List<WidgetName>> = MutableStateFlow(emptyList())
    val widgetNames: StateFlow<List<WidgetName>> = _widgetNames.asStateFlow()

    private fun updateWidgetNames(names: List<WidgetName>) {
        _widgetNames.update { names }
    }

    private fun fetchWidgets() {
        viewModelScope.launch {
            getWidgetsUseCase.getAsFlow().collect(::updateWidgetNames)
        }
    }

    fun saveWidgets(checkedWidgets: List<CheckedWidget>) {
        val widgets = checkedWidgets.filter { it.isChecked }.map { it.name }

        viewModelScope.launch {
            saveWidgetsUseCase.execute(widgetsToSave = widgets, currentWidgets = widgetNames.value)
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


    private val _budgetsOnWidget: MutableStateFlow<List<Int>> = MutableStateFlow(emptyList())
    val budgetsOnWidget = _budgetsOnWidget.asStateFlow()

    private fun updateBudgetsOnWidget(budgetsIds: List<Int>) {
        _budgetsOnWidget.update { budgetsIds }
    }

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

    private fun fetchBudgetsOnWidget() {
        viewModelScope.launch {
            getBudgetIdsOnWidgetUseCase.getAsFlow().collect(::updateBudgetsOnWidget)
        }
    }

    fun saveCurrentBudgetsOnWidget() {
        viewModelScope.launch {
            saveBudgetsOnWidgetUseCase.execute(budgetsIds = budgetsOnWidget.value)
        }
    }


    init {
        fetchWidgets()
        fetchBudgetsOnWidget()
    }

}