package com.ataglance.walletglance.personalization.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.personalization.domain.model.CheckedWidget
import com.ataglance.walletglance.personalization.domain.model.WidgetName
import com.ataglance.walletglance.personalization.domain.usecase.GetWidgetsUseCase
import com.ataglance.walletglance.personalization.domain.usecase.SaveWidgetsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PersonalizationViewModel(
    private val saveWidgetsUseCase: SaveWidgetsUseCase,
    private val getWidgetsUseCase: GetWidgetsUseCase
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


    init {
        fetchWidgets()
    }

}