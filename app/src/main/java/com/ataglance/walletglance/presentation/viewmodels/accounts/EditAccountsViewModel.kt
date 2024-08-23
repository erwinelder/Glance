package com.ataglance.walletglance.presentation.viewmodels.accounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ataglance.walletglance.data.accounts.Account
import com.ataglance.walletglance.data.local.entities.AccountEntity
import com.ataglance.walletglance.domain.utils.deleteItemAndMoveOrderNum
import com.ataglance.walletglance.domain.utils.findByOrderNum
import com.ataglance.walletglance.domain.utils.toEntityList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EditAccountsViewModel(
    passedAccountList: List<Account>
) : ViewModel() {

    private val _accountsUiState: MutableStateFlow<List<Account>> =
        MutableStateFlow(passedAccountList.takeIf { it.isNotEmpty() } ?: getNewAccountList())
    val accountsUiState: StateFlow<List<Account>> = _accountsUiState.asStateFlow()

    private val _allowDeleting: MutableStateFlow<Boolean> =
        MutableStateFlow(accountsUiState.value.size > 1)
    val allowDeleting: StateFlow<Boolean> = _allowDeleting.asStateFlow()

    fun getNewAccount(): Account {
        return Account(
            name = "",
            isActive = false
        )
    }

    private fun getNewAccountList(): List<Account> {
        return listOf(
            Account(
                id = 1,
                orderNum = 1
            )
        )
    }

    fun deleteAccountById(id: Int): List<Account>? {
        if (accountsUiState.value.size < 2) return null

        val newList = accountsUiState.value.deleteItemAndMoveOrderNum(
            { it.id == id }, { it.copy(orderNum = it.orderNum - 1) }
        ).toMutableList()

        if (newList.find { it.isActive } == null) {
            newList[0] = newList[0].copy(isActive = true)
        }

        _accountsUiState.update { newList }
        _allowDeleting.update { newList.size > 1 }

        return newList
    }

    fun swapAccounts(thisOrderNum: Int, otherOrderNum: Int) {
        _accountsUiState.update {
            it.swapAccounts(thisOrderNum, otherOrderNum) ?: it
        }
    }

    private fun List<Account>.swapAccounts(
        firstOrderNum: Int,
        secondOrderNum: Int
    ): List<Account>? {
        val (firstAccountToSwap, secondAccountToSwap) = accountsUiState.value.let {
            (it.findByOrderNum(firstOrderNum) ?: return null) to
                    (it.findByOrderNum(secondOrderNum) ?: return null)
        }

        return this.map { account ->
            if (
                account.orderNum != firstAccountToSwap.orderNum &&
                account.orderNum != secondAccountToSwap.orderNum
            ) {
                account
            } else if (account.orderNum == firstAccountToSwap.orderNum) {
                secondAccountToSwap.copy(orderNum = account.orderNum)
            } else {
                firstAccountToSwap.copy(orderNum = account.orderNum)
            }
        }
    }

    fun saveAccount(accountToSave: Account) {
        if (accountToSave.id == 0) {
            _accountsUiState.update {
                it + listOf(
                    accountToSave.copy(
                        id = (it.maxOfOrNull { acc -> acc.id } ?: 0) + 1,
                        orderNum = (it.maxOfOrNull { acc -> acc.orderNum } ?: 0) + 1,
                    )
                )
            }
        } else {
            _accountsUiState.update { state ->
                state.map { it.takeIf { it.orderNum != accountToSave.orderNum } ?: accountToSave }
            }
        }
    }

    fun getAccountEntities(): List<AccountEntity> {
        var accountList = accountsUiState.value

        if (accountList.filter { it.isActive }.size != 1) {
            accountList = accountList.mapIndexed { index, account ->
                account.copy(isActive = index == 0)
            }
        }

        if (accountList.find { it.isActive && it.hide } != null) {
            accountList.find { !it.hide }?.let { visibleAccount ->
                accountList = accountList.map { account ->
                    account.takeIf { it.id != visibleAccount.id }
                        ?: visibleAccount.copy(isActive = true)
                }
            }
        }

        return accountList.toEntityList()
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