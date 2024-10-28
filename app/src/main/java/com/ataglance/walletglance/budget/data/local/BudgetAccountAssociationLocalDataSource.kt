package com.ataglance.walletglance.budget.data.local

import com.ataglance.walletglance.budget.data.model.BudgetAccountAssociation
import com.ataglance.walletglance.core.data.local.BaseLocalDataSource
import com.ataglance.walletglance.core.data.local.TableUpdateTimeDao
import com.ataglance.walletglance.core.data.model.TableName

class BudgetAccountAssociationLocalDataSource(
    budgetAccountAssociationDao: BudgetAccountAssociationDao,
    updateTimeDao: TableUpdateTimeDao
) : BaseLocalDataSource<BudgetAccountAssociation>(
    dao = budgetAccountAssociationDao,
    updateTimeDao = updateTimeDao,
    tableName = TableName.BudgetAccountAssociation
)