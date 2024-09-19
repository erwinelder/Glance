package com.ataglance.walletglance.personalization.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ataglance.walletglance.personalization.domain.model.CheckedWidget
import com.ataglance.walletglance.personalization.domain.model.WidgetName
import com.ataglance.walletglance.core.utils.moveItems
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EditWidgetsViewModel(
    initialWidgetNamesList: List<WidgetName>
) : ViewModel() {

    private val _widgetList: MutableStateFlow<List<CheckedWidget>> = MutableStateFlow(
        WidgetName.entries.map { widgetName ->
            CheckedWidget(widgetName, widgetName in initialWidgetNamesList)
        }
    )
    val widgetList: StateFlow<List<CheckedWidget>> = _widgetList.asStateFlow()

    fun changeWidgetCheckState(widgetName: WidgetName, isChecked: Boolean) {
        _widgetList.update { widgetList ->
            widgetList.map { widget ->
                widget.takeIf { it.name != widgetName } ?: widget.copy(isChecked = isChecked)
            }
        }
    }

    fun moveWidgets(fromIndex: Int, toIndex: Int) {
        _widgetList.update {
            it.moveItems(fromIndex, toIndex)
        }
    }

    fun getWidgetList(): List<CheckedWidget> {
        return widgetList.value
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
