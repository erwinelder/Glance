package com.ataglance.walletglance.budget.data.local

import com.ataglance.walletglance.budget.data.model.BudgetEntity
import com.ataglance.walletglance.core.data.local.source.BaseLocalDataSource
import com.ataglance.walletglance.core.data.local.dao.LocalUpdateTimeDao
import com.ataglance.walletglance.core.data.model.TableName

class BudgetLocalDataSource(
    budgetDao: BudgetDao,
    updateTimeDao: LocalUpdateTimeDao
) : BaseLocalDataSource<BudgetEntity>(
    dao = budgetDao,
    updateTimeDao = updateTimeDao,
    tableName = TableName.Budget
)