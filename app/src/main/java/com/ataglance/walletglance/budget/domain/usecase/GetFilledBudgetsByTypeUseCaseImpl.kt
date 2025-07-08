package com.ataglance.walletglance.budget.domain.usecase

import com.ataglance.walletglance.budget.domain.model.BudgetsByType
import com.ataglance.walletglance.budget.domain.utils.groupByType
import com.ataglance.walletglance.transaction.domain.usecase.GetTransactionsInDateRangeUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class GetFilledBudgetsByTypeUseCaseImpl(
    private val getEmptyBudgetsUseCase: GetEmptyBudgetsUseCase,
    private val getTransactionsInDateRangeUseCase: GetTransactionsInDateRangeUseCase
) : GetFilledBudgetsByTypeUseCase {

    override fun getAsFlow(): Flow<BudgetsByType> = flow {
        val budgetsByType = getEmptyBudgetsUseCase.get().groupByType()

        budgetsByType.getMaxDateRange()?.let { range ->
            val flow = getTransactionsInDateRangeUseCase.getAsFlow(range = range).map { transactions ->
                budgetsByType.fillUsedAmountsByTransactions(transactions = transactions)
            }
            emitAll(flow = flow)
        } ?: emit(budgetsByType)
    }

    override suspend fun get(): BudgetsByType {
        val budgetsByType = getEmptyBudgetsUseCase.get().groupByType()
        val range = budgetsByType.getMaxDateRange() ?: return BudgetsByType()
        val transactions = getTransactionsInDateRangeUseCase.get(range = range)

        return budgetsByType.fillUsedAmountsByTransactions(transactions = transactions)
    }

}