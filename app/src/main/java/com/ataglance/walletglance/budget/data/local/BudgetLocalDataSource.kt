package com.ataglance.walletglance.budget.data.local

import com.ataglance.walletglance.budget.data.model.BudgetEntity
import com.ataglance.walletglance.core.data.local.BaseLocalDataSource
import com.ataglance.walletglance.core.data.local.TableUpdateTimeDao
import com.ataglance.walletglance.core.data.model.TableName

class BudgetLocalDataSource(
    budgetDao: BudgetDao,
    updateTimeDao: TableUpdateTimeDao
) : BaseLocalDataSource<BudgetEntity>(
    dao = budgetDao,
    updateTimeDao = updateTimeDao,
    tableName = TableName.Budget
)