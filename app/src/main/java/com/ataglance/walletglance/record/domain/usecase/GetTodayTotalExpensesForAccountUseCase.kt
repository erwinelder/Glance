package com.ataglance.walletglance.record.domain.usecase

interface GetTodayTotalExpensesForAccountUseCase {
    suspend fun get(accountId: Int): Double
}