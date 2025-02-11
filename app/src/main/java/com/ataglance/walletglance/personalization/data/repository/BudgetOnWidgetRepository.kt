package com.ataglance.walletglance.personalization.data.repository

import com.ataglance.walletglance.personalization.data.local.model.BudgetOnWidgetEntity
import kotlinx.coroutines.flow.Flow

interface BudgetOnWidgetRepository {

    suspend fun deleteAndUpsertBudgetsOnWidget(
        toDelete: List<BudgetOnWidgetEntity>,
        toUpsert: List<BudgetOnWidgetEntity>
    )

    suspend fun deleteAllBudgetsOnWidgetLocally()

    fun getAllBudgetsOnWidget(): Flow<List<BudgetOnWidgetEntity>>

}