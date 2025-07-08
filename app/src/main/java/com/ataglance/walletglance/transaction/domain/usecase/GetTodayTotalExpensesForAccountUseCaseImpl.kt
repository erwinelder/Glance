package com.ataglance.walletglance.transaction.domain.usecase

import com.ataglance.walletglance.core.domain.date.TimestampRange

class GetTodayTotalExpensesForAccountUseCaseImpl(
    private val getTransactionsInDateRangeUseCase: GetTransactionsInDateRangeUseCase
) : GetTodayTotalExpensesForAccountUseCase {
    override suspend fun get(accountId: Int): Double {
        return getTransactionsInDateRangeUseCase
            .get(range = TimestampRange.asToday())
            .sumOf { it.getTotalExpensesByAccount(accountId = accountId) }
    }
}