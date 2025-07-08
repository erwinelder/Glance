package com.ataglance.walletglance.budget.data.repository

import com.ataglance.walletglance.budget.data.model.BudgetOnWidgetDataModel
import kotlinx.coroutines.flow.Flow

interface BudgetOnWidgetRepository {

    suspend fun deleteAndUpsertBudgetsOnWidget(
        toDelete: List<BudgetOnWidgetDataModel>,
        toUpsert: List<BudgetOnWidgetDataModel>
    )

    fun getAllBudgetsOnWidgetAsFlow(): Flow<List<BudgetOnWidgetDataModel>>

    suspend fun getAllBudgetsOnWidget(): List<BudgetOnWidgetDataModel>

}