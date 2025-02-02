package com.ataglance.walletglance.personalization.data.repository

import com.ataglance.walletglance.core.utils.getCurrentTimestamp
import com.ataglance.walletglance.personalization.data.local.BudgetOnWidgetLocalDataSource
import com.ataglance.walletglance.personalization.data.model.BudgetOnWidgetEntity
import com.ataglance.walletglance.personalization.data.remote.BudgetOnWidgetRemoteDataSource

class BudgetOnWidgetRepositoryImpl(
    override val localSource: BudgetOnWidgetLocalDataSource,
    override val remoteSource: BudgetOnWidgetRemoteDataSource?
) : BudgetOnWidgetRepository {

    override suspend fun upsertBudgetsOnWidgetAndDeleteOther(
        budgetsToUpsert: List<BudgetOnWidgetEntity>,
    ) {
        val timestamp = getCurrentTimestamp()

        localSource.getAllEntities().collect { budgetOnWidgetList ->
            val budgetsToDelete = budgetOnWidgetList.filter { budget ->
                budgetsToUpsert.none { it.budgetId == budget.budgetId }
            }

            localSource.deleteAndUpsertEntities(
                entitiesToDelete = budgetsToDelete,
                entitiesToUpsert = budgetsToUpsert,
                timestamp = timestamp
            )
            remoteSource?.deleteAndUpsertEntities(
                entitiesToDelete = budgetsToDelete,
                entitiesToUpsert = budgetsToUpsert,
                timestamp = timestamp
            )
        }
    }

    override suspend fun deleteAllEntities() {
        val timestamp = getCurrentTimestamp()
        localSource.deleteAllBudgetsOnWidget(timestamp = timestamp)
        remoteSource?.deleteAllEntities(timestamp = timestamp)
    }

    override suspend fun deleteAllEntitiesLocally() {
        val timestamp = getCurrentTimestamp()
        localSource.deleteAllBudgetsOnWidget(timestamp = timestamp)
    }

}