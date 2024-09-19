package com.ataglance.walletglance.personalization.data.repository

import androidx.room.Transaction
import com.ataglance.walletglance.personalization.data.local.dao.BudgetOnWidgetDao
import com.ataglance.walletglance.personalization.data.local.model.BudgetOnWidgetEntity
import kotlinx.coroutines.flow.Flow

class BudgetOnWidgetRepository(
    private val dao: BudgetOnWidgetDao
) {

    @Transaction
    suspend fun upsertBudgetsOnWidgetAndDeleteOther(
        budgetOnWidgetListToUpsert: List<BudgetOnWidgetEntity>,
    ) {
        dao.deleteBudgetsThatAreNotInList(budgetOnWidgetListToUpsert.map { it.budgetId })
        dao.upsertBudgetsOnWidget(budgetOnWidgetListToUpsert)
    }

    fun getAllBudgetsOnWidget(): Flow<List<BudgetOnWidgetEntity>> {
        return dao.getAllBudgetsOnWidget()
    }

}