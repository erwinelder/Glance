package com.ataglance.walletglance.record.domain.usecase

class GetTodayTotalExpensesForAccountUseCaseMock : GetTodayTotalExpensesForAccountUseCase {

    var total = 0.0

    companion object {

        fun returnTotal(total: Double = 0.0): GetTodayTotalExpensesForAccountUseCaseMock {
            return GetTodayTotalExpensesForAccountUseCaseMock().apply {
                this.total = total
            }
        }

    }

    override suspend fun get(accountId: Int): Double {
        return total
    }

}