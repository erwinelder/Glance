package com.ataglance.walletglance.account.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.usecase.GetAccountsUseCase
import com.ataglance.walletglance.account.domain.usecase.SaveAccountsUseCase
import com.ataglance.walletglance.account.domain.utils.fixOrderNums
import com.ataglance.walletglance.account.domain.utils.makeSureActiveAccountIsVisibleOne
import com.ataglance.walletglance.account.domain.utils.makeSureThereIsOnlyOneActiveAccount
import com.ataglance.walletglance.core.utils.deleteItemAndMoveOrderNum
import com.ataglance.walletglance.core.utils.moveItems
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditAccountsViewModel(
    private val saveAccountsUseCase: SaveAccountsUseCase,
    private val getAccountsUseCase: GetAccountsUseCase
) : ViewModel() {

    init {
        viewModelScope.launch {
            val accounts = getAccountsUseCase.getAll().takeIf { it.isNotEmpty() }
                ?: listOf(Account(id = 1, orderNum = 1))
            _accounts.update { accounts }
        }
    }


    private val _accounts = MutableStateFlow<List<Account>>(emptyList())
    val accounts = _accounts.asStateFlow()

    fun deleteAccount(id: Int): List<Account>? {
        if (accounts.value.size < 2) return null

        val newList = accounts.value.deleteItemAndMoveOrderNum(
            { it.id == id }, { it.copy(orderNum = it.orderNum - 1) }
        ).toMutableList()

        if (newList.none { it.isActive }) {
            newList[0] = newList[0].copy(isActive = true)
        }

        _accounts.update { newList }

        return newList
    }

    fun moveAccounts(firstIndex: Int, secondIndex: Int) {
        _accounts.update {
            it.moveItems(firstIndex, secondIndex)
        }
    }

    fun applyAccount(account: Account) {
        if (account.id == 0) {
            _accounts.update { uiState ->
                uiState + listOf(
                    account.copy(
                        id = (uiState.maxOfOrNull { it.id } ?: 0) + 1,
                        orderNum = (uiState.maxOfOrNull { it.orderNum } ?: 0) + 1,
                    )
                )
            }
        } else {
            _accounts.update { state ->
                state.map { it.takeIf { it.orderNum != account.orderNum } ?: account }
            }
        }
    }

    fun getNewAccount(): Account {
        return Account(
            name = "",
            isActive = false
        )
    }

    suspend fun saveAccounts() {
        val accounts = accounts.value
            .takeIf { it.isNotEmpty() }
            ?.fixOrderNums()
            ?.makeSureThereIsOnlyOneActiveAccount()
            ?.makeSureActiveAccountIsVisibleOne()
            ?: return

        saveAccountsUseCase.saveDomainModels(accounts = accounts)
    }


    val allowDeleting = combine(_accounts) { accounts ->
        accounts.size > 1
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = false
    )

}