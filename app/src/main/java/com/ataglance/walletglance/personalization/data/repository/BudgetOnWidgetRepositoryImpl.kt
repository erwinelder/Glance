package com.ataglance.walletglance.personalization.data.repository

import com.ataglance.walletglance.core.utils.getNowDateTimeLong
import com.ataglance.walletglance.personalization.data.local.BudgetOnWidgetLocalDataSource
import com.ataglance.walletglance.personalization.data.model.BudgetOnWidgetEntity
import com.ataglance.walletglance.personalization.data.remote.BudgetOnWidgetRemoteDataSource

class BudgetOnWidgetRepositoryImpl(
    override val localSource: BudgetOnWidgetLocalDataSource,
    override val remoteSource: BudgetOnWidgetRemoteDataSource?
) : BudgetOnWidgetRepository {

    override suspend fun upsertBudgetsOnWidgetAndDeleteOther(
        budgetsToUpsert: List<BudgetOnWidgetEntity>,
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ) {
        val timestamp = getNowDateTimeLong()

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
                timestamp = timestamp,
                onSuccessListener = onSuccessListener,
                onFailureListener = onFailureListener
            )
        }
    }

    override suspend fun deleteAllEntities(
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ) {
        val timestamp = getNowDateTimeLong()
        localSource.deleteAllBudgetsOnWidget(timestamp)
        remoteSource?.deleteAllEntities(timestamp, onSuccessListener, onFailureListener)
    }

}