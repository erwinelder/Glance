package com.ataglance.walletglance.personalization.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ataglance.walletglance.core.utils.moveItems
import com.ataglance.walletglance.navigation.domain.model.BottomBarNavigationButton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EditNavigationButtonsViewModel(
    initialNavigationButtonList: List<BottomBarNavigationButton>
) : ViewModel() {

    private val _navigationButtonList: MutableStateFlow<List<BottomBarNavigationButton>> =
        MutableStateFlow(
            initialNavigationButtonList.subList(1, initialNavigationButtonList.lastIndex)
        )
    val navigationButtonList = _navigationButtonList.asStateFlow()

    fun moveButtons(fromIndex: Int, toIndex: Int) {
        _navigationButtonList.update {
            it.moveItems(fromIndex, toIndex)
        }
    }

    fun getNavigationButtonList(): List<BottomBarNavigationButton> {
        return navigationButtonList.value
    }

}

data class EditNavigationButtonsViewModelFactory(
    private val initialNavigationButtonList: List<BottomBarNavigationButton>
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditNavigationButtonsViewModel(initialNavigationButtonList) as T
    }
}
