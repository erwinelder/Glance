package com.ataglance.walletglance.budget.data.repository

import com.ataglance.walletglance.budget.data.local.model.BudgetOnWidgetEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BudgetOnWidgetRepositoryMock : BudgetOnWidgetRepository {

    var budgets = listOf(
        BudgetOnWidgetEntity(budgetId = 1),
        BudgetOnWidgetEntity(budgetId = 2)
    )


    override suspend fun deleteBudgetsOnWidget(budgets: List<BudgetOnWidgetEntity>) {}

    override suspend fun deleteAndUpsertBudgetsOnWidget(
        toDelete: List<BudgetOnWidgetEntity>,
        toUpsert: List<BudgetOnWidgetEntity>
    ) {}

    override suspend fun deleteAllBudgetsOnWidgetLocally() {}

    override fun getAllBudgetsOnWidgetFlow(): Flow<List<BudgetOnWidgetEntity>> = flow {
        emit(budgets)
    }

    override suspend fun getAllBudgetsOnWidget(): List<BudgetOnWidgetEntity> {
        return budgets
    }

}