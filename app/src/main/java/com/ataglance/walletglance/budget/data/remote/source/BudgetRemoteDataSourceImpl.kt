package com.ataglance.walletglance.budget.data.remote.source

import com.ataglance.walletglance.budget.data.remote.dao.BudgetRemoteDao
import com.ataglance.walletglance.budget.data.remote.model.BudgetAccountRemoteAssociation
import com.ataglance.walletglance.budget.data.remote.model.BudgetRemoteEntity
import com.ataglance.walletglance.core.data.model.EntitiesToSync
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.core.data.remote.dao.RemoteUpdateTimeDao

class BudgetRemoteDataSourceImpl(
    private val budgetDao: BudgetRemoteDao,
    private val updateTimeDao: RemoteUpdateTimeDao
) : BudgetRemoteDataSource {

    override suspend fun getBudgetUpdateTime(userId: String): Long? {
        return updateTimeDao.getUpdateTime(tableName = TableName.Budget.name, userId = userId)
    }

    override suspend fun saveBudgetUpdateTime(timestamp: Long, userId: String) {
        updateTimeDao.saveUpdateTime(
            tableName = TableName.Budget.name, timestamp = timestamp, userId = userId
        )
    }

    override suspend fun getBudgetsAfterTimestamp(
        timestamp: Long,
        userId: String
    ): EntitiesToSync<BudgetRemoteEntity> {
        return budgetDao.getBudgetsAfterTimestamp(timestamp = timestamp, userId = userId)
    }


    override suspend fun getBudgetAccountAssociationUpdateTime(userId: String): Long? {
        return updateTimeDao.getUpdateTime(
            tableName = TableName.BudgetAccountAssociation.name, userId = userId
        )
    }

    override suspend fun saveBudgetAccountAssociationUpdateTime(timestamp: Long, userId: String) {
        updateTimeDao.saveUpdateTime(
            tableName = TableName.BudgetAccountAssociation.name,
            timestamp = timestamp,
            userId = userId
        )
    }

    override suspend fun getBudgetAccountAssociationsAfterTimestamp(
        timestamp: Long,
        userId: String
    ): EntitiesToSync<BudgetAccountRemoteAssociation> {
        return budgetDao.getBudgetAccountAssociationsAfterTimestamp(
            timestamp = timestamp, userId = userId
        )
    }


    override suspend fun synchroniseBudgetsAndAssociations(
        budgetsToSync: EntitiesToSync<BudgetRemoteEntity>,
        associationsToSync: EntitiesToSync<BudgetAccountRemoteAssociation>,
        timestamp: Long,
        userId: String
    ) {
        budgetDao.synchroniseBudgetsAndAssociations(
            budgetsToSync = budgetsToSync,
            associationsToSync = associationsToSync,
            userId = userId
        )
        saveBudgetUpdateTime(timestamp = timestamp, userId = userId)
        saveBudgetAccountAssociationUpdateTime(timestamp = timestamp, userId = userId)
    }

}