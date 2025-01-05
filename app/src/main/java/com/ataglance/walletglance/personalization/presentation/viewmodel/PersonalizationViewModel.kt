package com.ataglance.walletglance.personalization.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.personalization.data.repository.BudgetOnWidgetRepository
import com.ataglance.walletglance.personalization.data.repository.WidgetRepository
import com.ataglance.walletglance.personalization.domain.model.CheckedWidget
import com.ataglance.walletglance.personalization.domain.model.WidgetName
import com.ataglance.walletglance.personalization.domain.utils.getItemsThatAreNotInList
import com.ataglance.walletglance.personalization.mapper.toEntityList
import com.ataglance.walletglance.personalization.mapper.toIntList
import com.ataglance.walletglance.personalization.mapper.toWidgetNamesList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PersonalizationViewModel(
    private val widgetRepository: WidgetRepository,
    private val budgetOnWidgetRepository: BudgetOnWidgetRepository
) : ViewModel() {

    private val _widgetNamesList: MutableStateFlow<List<WidgetName>> = MutableStateFlow(emptyList())
    val widgetNamesList: StateFlow<List<WidgetName>> = _widgetNamesList.asStateFlow()

    private fun fetchWidgetListFromDb() {
        viewModelScope.launch {
            widgetRepository.getAllEntities().collect { widgetList ->
                if (widgetList.isEmpty()) {

                    val defaultWidgetNamesList = getDefaultWidgetNamesList()
                    widgetRepository.upsertEntities(defaultWidgetNamesList.toEntityList())
                    _widgetNamesList.update { defaultWidgetNamesList }

                } else {

                    _widgetNamesList.update {
                        widgetList.sortedBy { it.orderNum }.toWidgetNamesList()
                    }

                }
            }
        }
    }

    private fun getDefaultWidgetNamesList(): List<WidgetName> {
        return listOf(
            WidgetName.ChosenBudgets,
            WidgetName.TotalForPeriod,
            WidgetName.RecentRecords,
            WidgetName.TopExpenseCategories,
        )
    }

    fun saveWidgetList(checkedWidgetList: List<CheckedWidget>) {
        val newWidgetNamesList = checkedWidgetList.filter { it.isChecked }.map { it.name }

        val widgetsToUpsert = newWidgetNamesList.toEntityList()
        val widgetsToDelete = widgetNamesList.value
            .getItemsThatAreNotInList(newWidgetNamesList)
            .toEntityList()

        viewModelScope.launch {
            widgetRepository.deleteAndUpsertEntities(
                toDelete = widgetsToDelete,
                toUpsert = widgetsToUpsert
            )
        }

        _widgetNamesList.update { newWidgetNamesList }
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

    private fun fetchBudgetsOnWidgetFromDb() {
        viewModelScope.launch {
            budgetOnWidgetRepository.getAllEntities().collect { budgetOnWidgetList ->
                _budgetsOnWidget.update { budgetOnWidgetList.toIntList() }
            }
        }
    }

    fun saveCurrentBudgetsOnWidgetToDb() {
        val budgetsOnWidgetToUpsert = budgetsOnWidget.value.toEntityList()

        viewModelScope.launch {
            budgetOnWidgetRepository.upsertBudgetsOnWidgetAndDeleteOther(budgetsOnWidgetToUpsert)
        }
    }


    init {
        fetchWidgetListFromDb()
        fetchBudgetsOnWidgetFromDb()
    }

}