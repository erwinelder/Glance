package com.ataglance.walletglance.transaction.domain.usecase

interface GetTodayTotalExpensesForAccountUseCase {
    suspend fun get(accountId: Int): Double
}