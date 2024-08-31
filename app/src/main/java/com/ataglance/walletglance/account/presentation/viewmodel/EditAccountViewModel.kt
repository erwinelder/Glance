package com.ataglance.walletglance.account.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.account.domain.Account
import com.ataglance.walletglance.account.domain.EditAccountUiState
import com.ataglance.walletglance.account.domain.color.AccountPossibleColors
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class EditAccountViewModel : ViewModel() {

    private val _editAccountUiState: MutableStateFlow<EditAccountUiState> = MutableStateFlow(
        EditAccountUiState()
    )
    val editAccountUiState = _editAccountUiState.asStateFlow()

    val allowSaving: StateFlow<Boolean> = combine(_editAccountUiState) { editAccountUiStateArray ->
        editAccountUiStateArray[0].allowSaving()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false
    )


    fun applyAccountData(account: Account) {
        _editAccountUiState.update { account.toEditAccountUiState() }
    }

    fun changeColor(colorName: String) {
        _editAccountUiState.update {
            it.copy(color = AccountPossibleColors().getByName(colorName))
        }
    }

    fun changeName(value: String) {
        _editAccountUiState.update {
            it.copy(name = value)
        }
    }
    fun changeCurrency(value: String) {
        _editAccountUiState.update {
            it.copy(currency = value)
        }
    }
    fun changeBalance(value: String) {
        _editAccountUiState.update {
            it.copy(
                balance = value.takeIf {
                    Regex("^-?(?:\\d{1,10}(?:\\.\\d{0,2})?)?\$").matches(value)
                } ?: return
            )
        }
    }
    fun changeHide(value: Boolean) {
        _editAccountUiState.update {
            it.copy(hide = value)
        }
    }
    fun changeHideBalance(value: Boolean) {
        if (editAccountUiState.value.withoutBalance) return

        _editAccountUiState.update {
            it.copy(hideBalance = value)
        }
    }
    fun changeWithoutBalance(value: Boolean) {
        _editAccountUiState.update {
            it.copy(
                hideBalance = false,
                withoutBalance = value
            )
        }
    }

    fun getAccount(): Account {
        return editAccountUiState.value.toAccount()
    }

}
