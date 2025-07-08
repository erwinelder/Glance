package com.ataglance.walletglance.budget.data.local.source

import com.ataglance.walletglance.budget.data.local.dao.BudgetLocalDao
import com.ataglance.walletglance.budget.data.local.model.BudgetEntity
import com.ataglance.walletglance.budget.data.local.model.BudgetEntityWithAssociations
import com.ataglance.walletglance.budget.data.utils.divide
import com.ataglance.walletglance.budget.data.utils.zipWithAssociations
import com.ataglance.walletglance.core.data.local.dao.LocalUpdateTimeDao
import com.ataglance.walletglance.core.data.local.database.AppDatabase
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.core.utils.excludeItems

class BudgetLocalDataSourceImpl(
    private val budgetDao: BudgetLocalDao,
    private val updateTimeDao: LocalUpdateTimeDao
) : BudgetLocalDataSource {

    override suspend fun getUpdateTime(): Long? {
        return updateTimeDao.getUpdateTime(tableName = TableName.Budget.name)
    }

    override suspend fun saveUpdateTime(timestamp: Long) {
        updateTimeDao.saveUpdateTime(tableName = TableName.Budget.name, timestamp = timestamp)
    }

    override suspend fun upsertBudgetsWithAssociations(
        budgetsWithAssociations: List<BudgetEntityWithAssociations>,
        timestamp: Long
    ) {
        val (budgets, associations) = budgetsWithAssociations.divide()

        budgetDao.upsertBudgetsAndAssociations(budgets = budgets, associations = associations)
        updateTimeDao.saveUpdateTime(tableName = TableName.Budget.name, timestamp = timestamp)
    }

    override suspend fun deleteBudgetsWithAssociations(
        budgetsWithAssociations: List<BudgetEntityWithAssociations>
    ) {
        val budgets = budgetsWithAssociations.map { it.budget }

        budgetDao.deleteBudgets(budgets = budgets)
    }

    override suspend fun deleteAndUpsertBudgetsWithAssociations(
        toDelete: List<BudgetEntityWithAssociations>,
        toUpsert: List<BudgetEntityWithAssociations>,
        timestamp: Long
    ) {
        val budgetsToDelete = toDelete.map { it.budget }
        val (budgetsToUpsert, associationsToUpsert) = toUpsert.divide()

        val associationsToDelete = budgetDao
            .getBudgetAccountAssociations(
                budgetIds = budgetsToUpsert.map { it.id }
            )
            .excludeItems(associationsToUpsert) { it.budgetId to it.accountId }

        budgetDao.deleteAndUpsertBudgetsAndAssociations(
            budgetsToDelete = budgetsToDelete,
            budgetsToUpsert = budgetsToUpsert,
            associationsToUpsert = associationsToUpsert,
            associationsToDelete = associationsToDelete
        )
        updateTimeDao.saveUpdateTime(tableName = TableName.Budget.name, timestamp = timestamp)
    }

    override suspend fun getAllBudgets(): List<BudgetEntity> {
        return budgetDao.getAllBudgets()
    }

    override suspend fun getBudgetsWithAssociationsAfterTimestamp(
        timestamp: Long
    ): List<BudgetEntityWithAssociations> {
        val budgets = budgetDao.getBudgetsAfterTimestamp(timestamp = timestamp)
        val ids = budgets.filterNot { it.deleted }.map { it.id }
        val associations = budgetDao.getBudgetAccountAssociations(budgetIds = ids)

        return budgets.zipWithAssociations(associations = associations)
    }

    override suspend fun getBudgetWithAssociations(budgetId: Int): BudgetEntityWithAssociations? {
        val budget = budgetDao.getBudget(id = budgetId) ?: return null
        val associations = budgetDao.getBudgetAccountAssociations(budgetId = budgetId)

        return BudgetEntityWithAssociations(budget = budget, associations = associations)
    }

    override suspend fun getAllBudgetsWithAssociations(): List<BudgetEntityWithAssociations> {
        val budgets = budgetDao.getAllBudgets()
        val associations = budgetDao.getAllBudgetAccountAssociations()

        return budgets.zipWithAssociations(associations = associations)
    }

}

fun getBudgetLocalDataSource(appDatabase: AppDatabase): BudgetLocalDataSource {
    return BudgetLocalDataSourceImpl(
        budgetDao = appDatabase.budgetDao,
        updateTimeDao = appDatabase.localUpdateTimeDao
    )
}
