package com.ataglance.walletglance.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ataglance.walletglance.data.Account
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
        return name.isNotBlank() && currency.isNotBlank() && balance.isNotBlank()
    }

    fun applyAccountData(account: Account) {
        _uiState.update {
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
                isActive = account.isActive,
                allowSaving = validateAccountData(
                    account.name, account.currency, account.balance.toString()
                )
            )
        }
    }

    fun changeColor(colorName: AccountColorName) {
        _uiState.update {
            it.copy(color = colorName.name)
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
                    Regex("^(?:[1-9]\\d{0,9}(?:[.,]\\d{0,2})?)?\$").matches(value)
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
        _uiState.update {
            it.copy(hideBalance = value)
        }
    }
    fun changeWithoutBalance(value: Boolean) {
        _uiState.update {
            it.copy(withoutBalance = value)
        }
    }

    fun getAccountObject(): Account {
        return Account(
            id = uiState.value.id,
            orderNum = uiState.value.orderNum,
            name = uiState.value.name,
            currency = uiState.value.currency,
            balance = uiState.value.balance.toDouble(),
            color = uiState.value.color,
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
    val color: String = AccountColorName.Default.name,
    val hide: Boolean = false,
    val hideBalance: Boolean = false,
    val withoutBalance: Boolean = false,
    val isActive: Boolean = false,
    val allowSaving: Boolean = false
)