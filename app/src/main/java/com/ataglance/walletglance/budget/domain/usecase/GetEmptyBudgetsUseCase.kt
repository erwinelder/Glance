package com.ataglance.walletglance.budget.domain.usecase

import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.budget.domain.model.Budget

interface GetEmptyBudgetsUseCase {

    suspend fun get(id: Int): Budget?

    suspend fun get(id: Int, accounts: List<Account>): Budget?

    suspend fun get(): List<Budget>

}