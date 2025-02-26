package com.ataglance.walletglance.budget.data.remote.source

import com.ataglance.walletglance.core.data.model.EntitiesToSync
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.core.data.remote.dao.RemoteUpdateTimeDao
import com.ataglance.walletglance.budget.data.remote.dao.BudgetOnWidgetRemoteDao
import com.ataglance.walletglance.budget.data.remote.model.BudgetOnWidgetRemoteEntity

class BudgetOnWidgetRemoteDataSourceImpl(
    private val budgetOnWidgetDao: BudgetOnWidgetRemoteDao,
    private val updateTimeDao: RemoteUpdateTimeDao
) : BudgetOnWidgetRemoteDataSource {

    override suspend fun getUpdateTime(userId: String): Long? {
        return updateTimeDao.getUpdateTime(tableName = TableName.BudgetOnWidget.name, userId = userId)
    }

    override suspend fun saveUpdateTime(timestamp: Long, userId: String) {
        updateTimeDao.saveUpdateTime(
            tableName = TableName.BudgetOnWidget.name, timestamp = timestamp, userId = userId
        )
    }

    override suspend fun upsertBudgetsOnWidget(
        budgets: List<BudgetOnWidgetRemoteEntity>,
        timestamp: Long,
        userId: String
    ) {
        budgetOnWidgetDao.upsertBudgetsOnWidgets(budgets = budgets, userId = userId)
        saveUpdateTime(timestamp = timestamp, userId = userId)
    }

    override suspend fun synchroniseBudgetsOnWidget(
        budgetsToSync: EntitiesToSync<BudgetOnWidgetRemoteEntity>,
        timestamp: Long,
        userId: String
    ) {
        budgetOnWidgetDao.synchroniseBudgetsOnWidget(budgetsToSync = budgetsToSync, userId = userId)
        saveUpdateTime(timestamp = timestamp, userId = userId)
    }

    override suspend fun getBudgetsOnWidgetAfterTimestamp(
        timestamp: Long,
        userId: String
    ): EntitiesToSync<BudgetOnWidgetRemoteEntity> {
        return budgetOnWidgetDao.getBudgetsOnWidgetAfterTimestamp(
            timestamp = timestamp, userId = userId
        )
    }

}