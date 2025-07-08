package com.ataglance.walletglance.budget.data.local.source

import com.ataglance.walletglance.budget.data.local.dao.BudgetOnWidgetLocalDao
import com.ataglance.walletglance.budget.data.local.model.BudgetOnWidgetEntity
import com.ataglance.walletglance.core.data.local.dao.LocalUpdateTimeDao
import com.ataglance.walletglance.core.data.local.database.AppDatabase
import com.ataglance.walletglance.core.data.model.TableName
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

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
        budgetOnWidgetDao.upsertBudgetsOnWidget(budgets = budgets)
        saveUpdateTime(timestamp = timestamp)
    }

    override suspend fun deleteBudgetsOnWidget(budgets: List<BudgetOnWidgetEntity>) {
        budgetOnWidgetDao.deleteBudgetsOnWidget(budgets = budgets)
    }

    override suspend fun deleteAndUpsertBudgetsOnWidget(
        toDelete: List<BudgetOnWidgetEntity>,
        toUpsert: List<BudgetOnWidgetEntity>,
        timestamp: Long
    ) {
        budgetOnWidgetDao.deleteAndUpsertBudgetsOnWidget(toDelete = toDelete, toUpsert = toUpsert)
        saveUpdateTime(timestamp = timestamp)
    }

    override suspend fun getBudgetsOnWidgetAfterTimestamp(timestamp: Long): List<BudgetOnWidgetEntity> {
        return budgetOnWidgetDao.getBudgetsOnWidgetAfterTimestamp(timestamp = timestamp)
    }

    override fun getAllBudgetsOnWidgetAsFlow(): Flow<List<BudgetOnWidgetEntity>> {
        return budgetOnWidgetDao.getAllBudgetsOnWidgetAsFlow()
    }

    override suspend fun getAllBudgetsOnWidget(): List<BudgetOnWidgetEntity> {
        return budgetOnWidgetDao.getAllBudgetsOnWidgetAsFlow().firstOrNull().orEmpty()
    }

}

fun getBudgetOnWidgetLocalDataSource(appDatabase: AppDatabase): BudgetOnWidgetLocalDataSource {
    return BudgetOnWidgetLocalDataSourceImpl(
        budgetOnWidgetDao = appDatabase.budgetOnWidgetDao,
        updateTimeDao = appDatabase.localUpdateTimeDao
    )
}