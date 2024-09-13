package com.ataglance.walletglance.account.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ataglance.walletglance.account.data.local.model.AccountEntity
import com.ataglance.walletglance.account.data.mapper.toAccountEntityList
import com.ataglance.walletglance.account.domain.Account
import com.ataglance.walletglance.account.utils.fixOrderNums
import com.ataglance.walletglance.account.utils.makeSureActiveAccountIsVisibleOne
import com.ataglance.walletglance.account.utils.makeSureThereIsOnlyOneActiveAccount
import com.ataglance.walletglance.core.utils.deleteItemAndMoveOrderNum
import com.ataglance.walletglance.core.utils.moveItems
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EditAccountsViewModel(
    initialAccountList: List<Account>
) : ViewModel() {

    private val _accountList: MutableStateFlow<List<Account>> = MutableStateFlow(
        initialAccountList.takeIf { it.isNotEmpty() } ?: listOf(Account(id = 1, orderNum = 1))
    )
    val accountList: StateFlow<List<Account>> = _accountList.asStateFlow()

    private val _allowDeleting: MutableStateFlow<Boolean> = MutableStateFlow(
        accountList.value.size > 1
    )
    val allowDeleting: StateFlow<Boolean> = _allowDeleting.asStateFlow()

    fun getNewAccount(): Account {
        return Account(
            name = "",
            isActive = false
        )
    }

    fun deleteAccountById(id: Int): List<Account>? {
        if (accountList.value.size < 2) return null

        val newList = accountList.value.deleteItemAndMoveOrderNum(
            { it.id == id }, { it.copy(orderNum = it.orderNum - 1) }
        ).toMutableList()

        if (newList.none { it.isActive }) {
            newList[0] = newList[0].copy(isActive = true)
        }

        _accountList.update { newList }
        _allowDeleting.update { newList.size > 1 }

        return newList
    }

    fun moveAccounts(firstIndex: Int, secondIndex: Int) {
        _accountList.update {
            it.moveItems(firstIndex, secondIndex)
        }
    }

    fun saveAccount(accountToSave: Account) {
        if (accountToSave.id == 0) {
            _accountList.update { uiState ->
                uiState + listOf(
                    accountToSave.copy(
                        id = (uiState.maxOfOrNull { it.id } ?: 0) + 1,
                        orderNum = (uiState.maxOfOrNull { it.orderNum } ?: 0) + 1,
                    )
                )
            }
        } else {
            _accountList.update { state ->
                state.map { it.takeIf { it.orderNum != accountToSave.orderNum } ?: accountToSave }
            }
        }
    }

    fun getAccountEntities(): List<AccountEntity>? {
        return accountList.value
            .takeIf { it.isNotEmpty() }
            ?.fixOrderNums()
            ?.makeSureThereIsOnlyOneActiveAccount()
            ?.makeSureActiveAccountIsVisibleOne()
            ?.toAccountEntityList()
    }

}

data class EditAccountsViewModelFactory(
    private val accountList: List<Account>,
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditAccountsViewModel(accountList) as T
    }
}