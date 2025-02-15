package com.ataglance.walletglance.account.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.model.color.AccountColors
import com.ataglance.walletglance.account.mapper.toAccount
import com.ataglance.walletglance.account.mapper.toEditAccountUiState
import com.ataglance.walletglance.account.presentation.model.AccountDraft
import com.ataglance.walletglance.core.utils.isNumberWithDecimalOptionalNegative
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class EditAccountViewModel : ViewModel() {

    private val _accountDraft = MutableStateFlow(AccountDraft())
    val accountDraft = _accountDraft.asStateFlow()

    fun applyAccount(account: Account) {
        _accountDraft.update { account.toEditAccountUiState() }
    }

    fun changeColor(colorName: String) {
        _accountDraft.update {
            it.copy(color = AccountColors.fromName(colorName))
        }
    }

    fun changeName(value: String) {
        _accountDraft.update {
            it.copy(name = value)
        }
    }

    fun changeCurrency(value: String) {
        _accountDraft.update {
            it.copy(currency = value)
        }
    }

    fun changeBalance(value: String) {
        val balance = value.takeIf { value.isNumberWithDecimalOptionalNegative() } ?: return
        _accountDraft.update {
            it.copy(balance = balance)
        }
    }

    fun changeHideStatus(value: Boolean) {
        _accountDraft.update {
            it.copy(hide = value)
        }
    }

    fun changeHideBalanceStatus(value: Boolean) {
        if (accountDraft.value.withoutBalance) return

        _accountDraft.update {
            it.copy(hideBalance = value)
        }
    }

    fun changeWithoutBalanceStatus(value: Boolean) {
        _accountDraft.update {
            it.copy(
                hideBalance = false,
                withoutBalance = value
            )
        }
    }

    fun getAccount(): Account? {
        return accountDraft.value.toAccount()
    }


    val allowSaving: StateFlow<Boolean> = combine(_accountDraft) { draft ->
        draft[0].allowSaving()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = false
    )

}
