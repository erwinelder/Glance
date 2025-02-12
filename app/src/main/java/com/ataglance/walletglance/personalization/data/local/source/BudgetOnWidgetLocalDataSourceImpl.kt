package com.ataglance.walletglance.personalization.data.local.source

import com.ataglance.walletglance.core.data.local.dao.LocalUpdateTimeDao
import com.ataglance.walletglance.core.data.local.database.AppDatabase
import com.ataglance.walletglance.core.data.model.EntitiesToSync
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.personalization.data.local.dao.BudgetOnWidgetLocalDao
import com.ataglance.walletglance.personalization.data.local.model.BudgetOnWidgetEntity
import kotlinx.coroutines.flow.Flow

class BudgetOnWidgetLocalDataSourceImpl(
    private val budgetOnWidgetDao: BudgetOnWidgetLocalDao,
    private val updateTimeDao: LocalUpdateTimeDao
) : BudgetOnWidgetLocalDataSource {

    override suspend fun getUpdateTime(): Long? {
        return updateTimeDao.getUpdateTime(tableName = TableName.BudgetOnWidget.name)
    }

    override suspend fun saveUpdateTime(timestamp: Long) {
        updateTimeDao.saveUpdateTime(tableName = TableName.BudgetOnWidget.name, timestamp = timestamp)
    }

    override suspend fun upsertBudgetsOnWidget(budgets: List<BudgetOnWidgetEntity>, timestamp: Long) {
        budgetOnWidgetDao.upsertBudgets(budgets = budgets)
        saveUpdateTime(timestamp = timestamp)
    }

    override suspend fun deleteBudgetsOnWidget(budgets: List<BudgetOnWidgetEntity>, timestamp: Long) {
        budgetOnWidgetDao.deleteBudgets(budgets = budgets)
        saveUpdateTime(timestamp = timestamp)
    }

    override suspend fun synchroniseBudgetsOnWidget(
        budgetsToSync: EntitiesToSync<BudgetOnWidgetEntity>,
        timestamp: Long
    ) {
        budgetOnWidgetDao.deleteAndUpsertBudgets(
            toDelete = budgetsToSync.toDelete,
            toUpsert = budgetsToSync.toUpsert
        )
        saveUpdateTime(timestamp = timestamp)
    }

    override suspend fun deleteAllBudgetsOnWidget(timestamp: Long) {
        budgetOnWidgetDao.deleteAllBudgetsOnWidget()
        saveUpdateTime(timestamp = timestamp)
    }

    override fun getAllBudgetsOnWidget(): Flow<List<BudgetOnWidgetEntity>> {
        return budgetOnWidgetDao.getAllBudgetsOnWidget()
    }

}

fun getBudgetOnWidgetLocalDataSource(appDatabase: AppDatabase): BudgetOnWidgetLocalDataSource {
    return BudgetOnWidgetLocalDataSourceImpl(
        budgetOnWidgetDao = appDatabase.budgetOnWidgetDao,
        updateTimeDao = appDatabase.localUpdateTimeDao
    )
}