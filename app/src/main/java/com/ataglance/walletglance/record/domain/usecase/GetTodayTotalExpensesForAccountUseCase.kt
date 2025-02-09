package com.ataglance.walletglance.record.domain.usecase

interface GetTodayTotalExpensesForAccountUseCase {
    suspend fun execute(accountId: Int): Double
}