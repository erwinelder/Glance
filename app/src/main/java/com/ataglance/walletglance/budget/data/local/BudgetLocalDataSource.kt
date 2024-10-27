package com.ataglance.walletglance.budget.data.local

import com.ataglance.walletglance.budget.data.model.BudgetEntity
import com.ataglance.walletglance.core.data.local.BaseLocalDataSource
import com.ataglance.walletglance.core.data.local.TableUpdateTimeDao
import com.ataglance.walletglance.core.data.model.TableName
import kotlinx.coroutines.flow.Flow

class BudgetLocalDataSource(
    private val budgetDao: BudgetDao,
    updateTimeDao: TableUpdateTimeDao
) : BaseLocalDataSource<BudgetEntity>(
    dao = budgetDao,
    updateTimeDao = updateTimeDao,
    tableName = TableName.Budget
) {

    fun getAllBudgets(): Flow<List<BudgetEntity>> = budgetDao.getAllBudgets()

}