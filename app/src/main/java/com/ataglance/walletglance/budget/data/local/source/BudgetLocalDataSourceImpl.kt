package com.ataglance.walletglance.budget.data.local.source

import com.ataglance.walletglance.budget.data.local.dao.BudgetLocalDao
import com.ataglance.walletglance.budget.data.local.model.BudgetAccountAssociation
import com.ataglance.walletglance.budget.data.local.model.BudgetEntity
import com.ataglance.walletglance.core.data.local.dao.LocalUpdateTimeDao
import com.ataglance.walletglance.core.data.local.database.AppDatabase
import com.ataglance.walletglance.core.data.model.EntitiesToSync
import com.ataglance.walletglance.core.data.model.TableName

class BudgetLocalDataSourceImpl(
    private val budgetDao: BudgetLocalDao,
    private val updateTimeDao: LocalUpdateTimeDao
) : BudgetLocalDataSource {

    override suspend fun getBudgetUpdateTime(): Long? {
        return updateTimeDao.getUpdateTime(tableName = TableName.Budget.name)
    }

    override suspend fun saveBudgetUpdateTime(timestamp: Long) {
        updateTimeDao.saveUpdateTime(tableName = TableName.Budget.name, timestamp = timestamp)
    }

    override suspend fun synchroniseBudgets(
        budgetsToSync: EntitiesToSync<BudgetEntity>,
        timestamp: Long
    ) {
        budgetDao.deleteAndUpsertBudgets(
            toDelete = budgetsToSync.toDelete,
            toUpsert = budgetsToSync.toUpsert
        )
        saveBudgetUpdateTime(timestamp = timestamp)
        if (budgetsToSync.toDelete.isNotEmpty()) {
            saveBudgetAccountAssociationUpdateTime(timestamp = timestamp)
        }
    }

    override suspend fun getBudget(id: Int): BudgetEntity? {
        return budgetDao.getBudget(id = id)
    }

    override suspend fun getAllBudgets(): List<BudgetEntity> {
        return budgetDao.getAllBudgets()
    }


    override suspend fun getBudgetAccountAssociationUpdateTime(): Long? {
        return updateTimeDao.getUpdateTime(tableName = TableName.BudgetAccountAssociation.name)
    }

    override suspend fun saveBudgetAccountAssociationUpdateTime(timestamp: Long) {
        updateTimeDao.saveUpdateTime(
            tableName = TableName.BudgetAccountAssociation.name, timestamp = timestamp
        )
    }

    override suspend fun synchroniseBudgetAccountAssociations(
        associationsToSync: EntitiesToSync<BudgetAccountAssociation>,
        timestamp: Long
    ) {
        budgetDao.deleteAndUpsertBudgetAccountAssociations(
            toDelete = associationsToSync.toDelete,
            toUpsert = associationsToSync.toUpsert
        )
        saveBudgetAccountAssociationUpdateTime(timestamp = timestamp)
    }

    override suspend fun getBudgetAccountAssociations(budgetId: Int): List<BudgetAccountAssociation> {
        return budgetDao.getBudgetAccountAssociations(budgetId = budgetId)
    }

    override suspend fun getAllBudgetAccountAssociations(): List<BudgetAccountAssociation> {
        return budgetDao.getAllBudgetAccountAssociations()
    }


    override suspend fun deleteBudgets(budgets: List<BudgetEntity>, timestamp: Long) {
        budgetDao.deleteBudgets(budgets = budgets)
        saveBudgetUpdateTime(timestamp = timestamp)
        saveBudgetAccountAssociationUpdateTime(timestamp = timestamp)
    }

    override suspend fun synchroniseBudgetsAndAssociations(
        budgetsToSync: EntitiesToSync<BudgetEntity>,
        associationsToSync: EntitiesToSync<BudgetAccountAssociation>,
        timestamp: Long
    ) {
        budgetDao.deleteAndUpsertBudgetsAndAssociations(
            budgetsToDelete = budgetsToSync.toDelete,
            budgetsToUpsert = budgetsToSync.toUpsert,
            associationsToDelete = associationsToSync.toDelete,
            associationsToUpsert = associationsToSync.toUpsert
        )
        saveBudgetUpdateTime(timestamp = timestamp)
        saveBudgetAccountAssociationUpdateTime(timestamp = timestamp)
    }

}

fun getBudgetLocalDataSource(appDatabase: AppDatabase): BudgetLocalDataSource {
    return BudgetLocalDataSourceImpl(
        budgetDao = appDatabase.budgetDao,
        updateTimeDao = appDatabase.localUpdateTimeDao
    )
}