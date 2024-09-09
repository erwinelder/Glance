package com.ataglance.walletglance.appearanceSettings.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ataglance.walletglance.appearanceSettings.domain.model.WidgetName
import com.ataglance.walletglance.core.utils.moveItems
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EditWidgetsViewModel(
    initialWidgetNamesList: List<WidgetName>
) : ViewModel() {

    private val _widgetNamesList: MutableStateFlow<List<WidgetName>> = MutableStateFlow(
        initialWidgetNamesList
    )
    val widgetNamesList: StateFlow<List<WidgetName>> = _widgetNamesList.asStateFlow()

    fun moveWidgets(fromIndex: Int, toIndex: Int) {
        _widgetNamesList.update {
            it.moveItems(fromIndex, toIndex)
        }
    }

    fun getWidgetNamesList(): List<WidgetName> {
        return widgetNamesList.value
    }

}

data class EditWidgetsViewModelFactory(
    private val widgetList: List<WidgetName>
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditWidgetsViewModel(widgetList) as T
    }
}
