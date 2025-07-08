package com.ataglance.walletglance.budget.domain.usecase

import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.budget.domain.utils.fillUsedAmountsByTransactions
import com.ataglance.walletglance.budget.domain.utils.getMaxDateRange
import com.ataglance.walletglance.transaction.domain.usecase.GetTransactionsInDateRangeUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest

class GetBudgetsOnWidgetUseCaseImpl(
    private val getEmptyBudgetsUseCase: GetEmptyBudgetsUseCase,
    private val getBudgetIdsOnWidgetUseCase: GetBudgetIdsOnWidgetUseCase,
    private val getTransactionsInDateRangeUseCase: GetTransactionsInDateRangeUseCase
) : GetBudgetsOnWidgetUseCase {

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getEmptyBudgetsOnWidgetAsFlow(): Flow<List<Budget>> = flow {
        val budgets = getEmptyBudgetsUseCase.get()

        emitAll(
            flow = getBudgetIdsOnWidgetUseCase.getAsFlow().mapLatest { budgetIds ->
                budgets.filter { it.id in budgetIds }
            }
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAsFlow(): Flow<List<Budget>> {
        return getEmptyBudgetsOnWidgetAsFlow().flatMapLatest { budgets ->
            val range = budgets.getMaxDateRange()

            getTransactionsInDateRangeUseCase.getAsFlowOrEmpty(range = range)
                .mapLatest { transactions ->
                    budgets.fillUsedAmountsByTransactions(transactions = transactions)
                }
        }
    }

}