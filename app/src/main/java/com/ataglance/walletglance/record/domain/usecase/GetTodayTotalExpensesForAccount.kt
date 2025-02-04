package com.ataglance.walletglance.record.domain.usecase

interface GetTodayTotalExpensesForAccount {
    suspend fun execute(accountId: Int): Double
}