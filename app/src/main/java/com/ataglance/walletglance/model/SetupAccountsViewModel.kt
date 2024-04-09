package com.ataglance.walletglance.model

import androidx.lifecycle.ViewModel
import com.ataglance.walletglance.data.Account
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SetupAccountsViewModel : ViewModel() {
    private val _accountsListState: MutableStateFlow<List<Account>> = MutableStateFlow(emptyList())
    val accountsListState: StateFlow<List<Account>> = _accountsListState.asStateFlow()
    private val _showDeleteAccountButton: MutableStateFlow<Boolean> =
        MutableStateFlow(accountsListState.value.size > 1)
    val showDeleteAccountButton: StateFlow<Boolean> = _showDeleteAccountButton.asStateFlow()

    fun applyAccountsList(accountList: List<Account>) {
        if (accountList.isNotEmpty()) {
            val sortedAccountList = accountList.sortedBy { it.orderNum }
            _accountsListState.update { sortedAccountList }
            _showDeleteAccountButton.update { sortedAccountList.size > 1 }
        } else if (accountsListState.value.isEmpty()) {
            addNewDefaultAccount()
        }
    }

    fun addNewDefaultAccount() {
        val accountsList = accountsListState.value.toMutableList()
        val newAccountId = accountsList.maxByOrNull { it.id }?.id?.let { it + 1 } ?: 1
        accountsList.add(
            Account(
                id = newAccountId,
                orderNum = accountsList.size + 1,
                isActive = accountsList.isEmpty()
            )
        )
        _accountsListState.update { accountsList }
        _showDeleteAccountButton.update { accountsList.size > 1 }
    }

    fun deleteAccountById(id: Int): List<Account>? {
        if (accountsListState.value.size == 1) {
            return null
        }

        val accountsList = mutableListOf<Account>()
        var stepAfterDeleting = 0
        val account = AccountController().getAccountById(id, accountsListState.value) ?: return null

        accountsListState.value.forEach {
            if (it.orderNum != account.orderNum) {
                accountsList.add(it.copy(orderNum = it.orderNum - stepAfterDeleting))
            } else {
                stepAfterDeleting = 1
            }
        }

        if (accountsList.find { it.isActive } == null) {
            accountsList[0] = accountsList[0].copy(isActive = true)
        }

        _accountsListState.update { accountsList }
        _showDeleteAccountButton.update { accountsList.size > 1 }

        return accountsList
    }

    fun getAccountByOrderNum(orderNum: Int): Account? {
        accountsListState.value.forEach {
            if (it.orderNum == orderNum) {
                return it
            }
        }
        return null
    }

    fun swapAccounts(thisOrderNum: Int, otherOrderNum: Int) {
        _accountsListState.update {
            getListWithSwappedAccounts(it, thisOrderNum, otherOrderNum) ?: it
        }
    }

    private fun getListWithSwappedAccounts(
        list: List<Account>,
        firstOrderNum: Int,
        secondOrderNum: Int
    ): List<Account>? {
        val firstAccountToSwap = getAccountByOrderNum(firstOrderNum) ?: return null
        val secondAccountToSwap = getAccountByOrderNum(secondOrderNum) ?: return null

        val finalList = mutableListOf<Account>()

        list.forEach { account ->
            if (
                account.orderNum != firstAccountToSwap.orderNum &&
                account.orderNum != secondAccountToSwap.orderNum
            ) {
                finalList.add(account)
            } else if (account.orderNum == firstAccountToSwap.orderNum) {
                finalList.add(secondAccountToSwap.copy(orderNum = account.orderNum))
            } else {
                finalList.add(firstAccountToSwap.copy(orderNum = account.orderNum))
            }
        }

        return finalList
    }

    fun saveAccountData(newAccount: Account) {
        var newAccountsList = mutableListOf<Account>()

        accountsListState.value.forEach { account ->
            if (account.orderNum != newAccount.orderNum) {
                newAccountsList.add(account)
            } else {
                if (newAccount.hide && accountsListState.value.filter { !it.hide }.size == 1) {
                    newAccountsList.add(newAccount.copy(hide = false))
                } else {
                    newAccountsList.add(newAccount)
                }
            }
        }

        if (newAccount.hide && newAccount.isActive && accountsListState.value.filter { !it.hide }.size > 1) {
            newAccountsList = newAccountsList.map {
                if (!it.hide) {
                    it.copy(isActive = true)
                } else {
                    it.copy(isActive = false)
                }
            }.toMutableList()
        }

        _accountsListState.update { newAccountsList }
    }

    fun resetAccountsList() {
        _accountsListState.update { listOf(Account(orderNum = 1)) }
    }
}