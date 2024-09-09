package com.ataglance.walletglance.core.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.appearanceSettings.data.repository.WidgetRepository
import com.ataglance.walletglance.appearanceSettings.domain.mapper.toEntityList
import com.ataglance.walletglance.appearanceSettings.domain.mapper.toWidgetNamesList
import com.ataglance.walletglance.appearanceSettings.domain.model.WidgetName
import com.ataglance.walletglance.appearanceSettings.domain.utils.getItemsThatAreNotInList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PersonalizationViewModel(
    private val widgetRepository: WidgetRepository
) : ViewModel() {

    private val _widgetNamesList: MutableStateFlow<List<WidgetName>> = MutableStateFlow(emptyList())
    val widgetNamesList: StateFlow<List<WidgetName>> = _widgetNamesList.asStateFlow()

    fun fetchWidgetListFromDb() {
        viewModelScope.launch {
            widgetRepository.getAllWidgets().collect { widgetList ->
                if (widgetList.isEmpty()) {

                    val defaultWidgetNamesList = getDefaultWidgetNamesList()
                    val widgetListToUpsert = defaultWidgetNamesList.toEntityList()
                    
                    widgetRepository.upsertWidgets(widgetListToUpsert)
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

    fun saveWidgetList(newWidgetNamesList: List<WidgetName>) {
        val widgetsToUpsert = newWidgetNamesList.toEntityList()
        val widgetsToDelete = widgetNamesList.value
            .getItemsThatAreNotInList(newWidgetNamesList)
            .toEntityList()

        viewModelScope.launch {
            widgetRepository.deleteAndUpsertWidgets(
                widgetListToDelete = widgetsToDelete,
                widgetListToUpsert = widgetsToUpsert
            )
        }

        _widgetNamesList.update { newWidgetNamesList }
    }

}