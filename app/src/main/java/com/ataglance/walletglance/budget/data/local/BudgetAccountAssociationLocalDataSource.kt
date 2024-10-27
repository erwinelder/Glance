package com.ataglance.walletglance.budget.data.local

import com.ataglance.walletglance.budget.data.model.BudgetAccountAssociation
import com.ataglance.walletglance.core.data.local.BaseLocalDataSource
import com.ataglance.walletglance.core.data.local.TableUpdateTimeDao
import com.ataglance.walletglance.core.data.model.TableName
import kotlinx.coroutines.flow.Flow

class BudgetAccountAssociationLocalDataSource(
    private val budgetAccountAssociationDao: BudgetAccountAssociationDao,
    updateTimeDao: TableUpdateTimeDao
) : BaseLocalDataSource<BudgetAccountAssociation>(
    dao = budgetAccountAssociationDao,
    updateTimeDao = updateTimeDao,
    tableName = TableName.BudgetAccountAssociation
) {

    fun getAllAssociations(): Flow<List<BudgetAccountAssociation>> =
        budgetAccountAssociationDao.getAllAssociations()

}