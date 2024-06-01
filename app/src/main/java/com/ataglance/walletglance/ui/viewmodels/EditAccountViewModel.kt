package com.ataglance.walletglance.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ataglance.walletglance.domain.entities.Account
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Locale

class EditAccountViewModel(
    uiState: EditAccountUiState
) : ViewModel() {
    private val _uiState: MutableStateFlow<EditAccountUiState> = MutableStateFlow(
        uiState.copy(
            allowSaving = validateAccountData(
                uiState.name, uiState.currency, uiState.balance
            )
        )
    )
    val uiState: StateFlow<EditAccountUiState> = _uiState.asStateFlow()

    private fun validateAccountData(
        name: String = uiState.value.name,
        currency: String = uiState.value.currency,
        balance: String = uiState.value.balance
    ): Boolean {
        return name.isNotBlank() &&
                currency.isNotBlank() &&
                balance.isNotBlank() &&
                balance.last() != '.'
    }

    fun applyAccountData(account: Account) {
        _uiState.update {
            it.copy(
                id = account.id,
                orderNum = account.orderNum,
                name = account.name,
                currency = account.currency,
                balance = "%.2f".format(Locale.US, account.balance),
                colorName = account.color,
                hide = account.hide,
                hideBalance = account.hideBalance,
                withoutBalance = account.withoutBalance,
                isActive = account.isActive,
                allowSaving = validateAccountData(
                    account.name, account.currency, account.balance.toString()
                )
            )
        }
    }

    fun changeColor(colorName: String) {
        _uiState.update {
            it.copy(colorName = colorName)
        }
    }

    fun changeName(value: String) {
        _uiState.update {
            it.copy(
                name = value,
                allowSaving = validateAccountData(name = value)
            )
        }
    }
    fun changeCurrency(value: String) {
        _uiState.update {
            it.copy(
                currency = value,
                allowSaving = validateAccountData(currency = value)
            )
        }
    }
    fun changeBalance(value: String) {
        _uiState.update {
            it.copy(
                balance = value.takeIf {
                    Regex("^(?:[0-9]\\d{0,9}(?:[.]\\d{0,2})?)?\$").matches(value)
                } ?: return,
                allowSaving = validateAccountData(balance = value)
            )
        }
    }
    fun changeHide(value: Boolean) {
        _uiState.update {
            it.copy(hide = value)
        }
    }
    fun changeHideBalance(value: Boolean) {
        if (uiState.value.withoutBalance) return

        _uiState.update {
            it.copy(hideBalance = value)
        }
    }
    fun changeWithoutBalance(value: Boolean) {
        _uiState.update {
            it.copy(
                hideBalance = false,
                withoutBalance = value
            )
        }
    }

    fun getAccountObject(): Account {
        return Account(
            id = uiState.value.id,
            orderNum = uiState.value.orderNum,
            name = uiState.value.name,
            currency = uiState.value.currency,
            balance = uiState.value.balance.toDouble(),
            color = uiState.value.colorName,
            hide = uiState.value.hide,
            hideBalance = uiState.value.hideBalance,
            withoutBalance = uiState.value.withoutBalance,
            isActive = uiState.value.isActive
        )
    }
}

data class EditAccountViewModelFactory(
    val uiState: EditAccountUiState
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditAccountViewModel(uiState) as T
    }
}

data class EditAccountUiState(
    val id: Int = 0,
    val orderNum: Int = 0,
    val name: String = "",
    val currency: String = "",
    val balance: String = "0.00",
    val colorName: String = AccountColorName.Default.name,
    val hide: Boolean = false,
    val hideBalance: Boolean = false,
    val withoutBalance: Boolean = false,
    val isActive: Boolean = false,
    val allowSaving: Boolean = false
)