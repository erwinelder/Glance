package com.ataglance.walletglance.account.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.account.domain.Account
import com.ataglance.walletglance.account.domain.color.AccountColorWithName
import com.ataglance.walletglance.account.domain.color.AccountColors
import com.ataglance.walletglance.account.domain.color.AccountPossibleColors
import com.ataglance.walletglance.account.utils.toAccountColorWithName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.util.Locale

class EditAccountViewModel : ViewModel() {

    private val _editAccountUiState: MutableStateFlow<EditAccountUiState> =
        MutableStateFlow(EditAccountUiState())
    val editAccountUiState: StateFlow<EditAccountUiState> = _editAccountUiState.asStateFlow()

    val allowSaving: StateFlow<Boolean> =
        combine(_editAccountUiState) { editAccountUiStateArray ->
            editAccountUiStateArray[0].let {
                it.name.isNotBlank() &&
                        it.currency.isNotBlank() &&
                        it.balance.isNotBlank() &&
                        it.balance.last() != '.'
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )


    fun applyAccountData(account: Account) {
        _editAccountUiState.update {
            it.copy(
                id = account.id,
                orderNum = account.orderNum,
                name = account.name,
                currency = account.currency,
                balance = "%.2f".format(Locale.US, account.balance),
                color = account.color,
                hide = account.hide,
                hideBalance = account.hideBalance,
                withoutBalance = account.withoutBalance,
                isActive = account.isActive
            )
        }
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

data class EditAccountUiState(
    val id: Int = 0,
    val orderNum: Int = 0,
    val name: String = "",
    val currency: String = "",
    val balance: String = "0.00",
    val color: AccountColorWithName = AccountColors.Default.toAccountColorWithName(),
    val hide: Boolean = false,
    val hideBalance: Boolean = false,
    val withoutBalance: Boolean = false,
    val isActive: Boolean = false
) {

    fun toAccount(): Account {
        return Account(
            id = id,
            orderNum = orderNum,
            name = name,
            currency = currency,
            balance = balance.toDouble(),
            color = color,
            hide = hide,
            hideBalance = hideBalance,
            withoutBalance = withoutBalance,
            isActive = isActive
        )
    }

}