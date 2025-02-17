package com.ataglance.walletglance.budget.data.repository

import com.ataglance.walletglance.auth.data.model.UserContext
import com.ataglance.walletglance.budget.data.local.model.BudgetOnWidgetEntity
import com.ataglance.walletglance.budget.data.local.source.BudgetOnWidgetLocalDataSource
import com.ataglance.walletglance.budget.data.mapper.budgetOnWidget.toLocalEntity
import com.ataglance.walletglance.budget.data.mapper.budgetOnWidget.toRemoteEntity
import com.ataglance.walletglance.budget.data.remote.model.BudgetOnWidgetRemoteEntity
import com.ataglance.walletglance.budget.data.remote.source.BudgetOnWidgetRemoteDataSource
import com.ataglance.walletglance.core.data.model.EntitiesToSync
import com.ataglance.walletglance.core.data.utils.synchroniseData
import com.ataglance.walletglance.core.utils.getCurrentTimestamp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class BudgetOnWidgetRepositoryImpl(
    private val localSource: BudgetOnWidgetLocalDataSource,
    private val remoteSource: BudgetOnWidgetRemoteDataSource,
    private val userContext: UserContext
) : BudgetOnWidgetRepository {

    private suspend fun synchroniseBudgetsOnWidget() {
        val userId = userContext.getUserId() ?: return

        synchroniseData(
            localUpdateTimeGetter = localSource::getUpdateTime,
            remoteUpdateTimeGetter = { remoteSource.getUpdateTime(userId = userId) },
            remoteDataGetter = { timestamp ->
                remoteSource.getBudgetsOnWidgetAfterTimestamp(timestamp = timestamp, userId = userId)
            },
            remoteDataToLocalDataMapper = BudgetOnWidgetRemoteEntity::toLocalEntity,
            localSynchroniser = localSource::synchroniseBudgetsOnWidget
        )
    }


    override suspend fun deleteAndUpsertBudgetsOnWidget(
        toDelete: List<BudgetOnWidgetEntity>,
        toUpsert: List<BudgetOnWidgetEntity>
    ) {
        val timestamp = getCurrentTimestamp()
        val budgetsToSync = EntitiesToSync(toDelete = toDelete, toUpsert = toUpsert)

        localSource.synchroniseBudgetsOnWidget(budgetsToSync = budgetsToSync, timestamp = timestamp)
        userContext.getUserId()?.let { userId ->
            remoteSource.synchroniseBudgetsOnWidget(
                budgetsToSync = budgetsToSync.map { deleted ->
                    toRemoteEntity(updateTime = timestamp, deleted = deleted)
                },
                timestamp = timestamp,
                userId = userId
            )
        }
    }

    override suspend fun deleteAllBudgetsOnWidgetLocally() {
        val timestamp = getCurrentTimestamp()
        localSource.deleteAllBudgetsOnWidget(timestamp = timestamp)
    }

    override fun getAllBudgetsOnWidgetFlow(): Flow<List<BudgetOnWidgetEntity>> = flow {
        coroutineScope {
            launch { synchroniseBudgetsOnWidget() }
            localSource.getAllBudgetsOnWidget().collect(::emit)
        }
    }

    override suspend fun getAllBudgetsOnWidget(): List<BudgetOnWidgetEntity> {
        synchroniseBudgetsOnWidget()
        return localSource.getAllBudgetsOnWidget().firstOrNull().orEmpty()
    }

}