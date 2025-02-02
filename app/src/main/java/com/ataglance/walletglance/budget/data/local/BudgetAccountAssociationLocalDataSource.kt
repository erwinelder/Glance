package com.ataglance.walletglance.budget.data.local

import com.ataglance.walletglance.budget.data.model.BudgetAccountAssociation
import com.ataglance.walletglance.core.data.local.source.BaseLocalDataSource
import com.ataglance.walletglance.core.data.local.dao.LocalUpdateTimeDao
import com.ataglance.walletglance.core.data.model.TableName

class BudgetAccountAssociationLocalDataSource(
    budgetAccountAssociationDao: BudgetAccountAssociationDao,
    updateTimeDao: LocalUpdateTimeDao
) : BaseLocalDataSource<BudgetAccountAssociation>(
    dao = budgetAccountAssociationDao,
    updateTimeDao = updateTimeDao,
    tableName = TableName.BudgetAccountAssociation
)