package com.ataglance.walletglance.budget.data.repository

import com.ataglance.walletglance.auth.data.model.UserContext
import com.ataglance.walletglance.budget.data.local.model.BudgetAccountAssociation
import com.ataglance.walletglance.budget.data.local.model.BudgetEntity
import com.ataglance.walletglance.budget.data.local.source.BudgetLocalDataSource
import com.ataglance.walletglance.budget.data.mapper.budget.toLocalAssociation
import com.ataglance.walletglance.budget.data.mapper.budget.toLocalEntity
import com.ataglance.walletglance.budget.data.mapper.budget.toRemoteAssociation
import com.ataglance.walletglance.budget.data.mapper.budget.toRemoteEntity
import com.ataglance.walletglance.budget.data.remote.model.BudgetAccountRemoteAssociation
import com.ataglance.walletglance.budget.data.remote.model.BudgetRemoteEntity
import com.ataglance.walletglance.budget.data.remote.source.BudgetRemoteDataSource
import com.ataglance.walletglance.core.data.model.EntitiesToSync
import com.ataglance.walletglance.core.data.utils.synchroniseData
import com.ataglance.walletglance.core.utils.getCurrentTimestamp

class BudgetRepositoryImpl(
    private val localSource: BudgetLocalDataSource,
    private val remoteSource: BudgetRemoteDataSource,
    private val userContext: UserContext
) : BudgetRepository {

    private suspend fun synchroniseBudgets() {
        val userId = userContext.getUserId() ?: return

        synchroniseData(
            localUpdateTimeGetter = localSource::getBudgetUpdateTime,
            remoteUpdateTimeGetter = { remoteSource.getBudgetUpdateTime(userId = userId) },
            remoteDataGetter = { timestamp ->
                remoteSource.getBudgetsAfterTimestamp(timestamp = timestamp, userId = userId)
            },
            remoteDataToLocalDataMapper = BudgetRemoteEntity::toLocalEntity,
            localSynchroniser = localSource::synchroniseBudgets
        )
    }

    private suspend fun synchroniseBudgetAccountAssociations() {
        val userId = userContext.getUserId() ?: return

        synchroniseData(
            localUpdateTimeGetter = localSource::getBudgetAccountAssociationUpdateTime,
            remoteUpdateTimeGetter = {
                remoteSource.getBudgetAccountAssociationUpdateTime(userId = userId)
            },
            remoteDataGetter = { timestamp ->
                remoteSource.getBudgetAccountAssociationsAfterTimestamp(
                    timestamp = timestamp, userId = userId
                )
            },
            remoteDataToLocalDataMapper = BudgetAccountRemoteAssociation::toLocalAssociation,
            localSynchroniser = localSource::synchroniseBudgetAccountAssociations
        )
    }


    override suspend fun deleteAndUpsertBudgetsAndAssociations(
        budgetsToDelete: List<BudgetEntity>,
        budgetsToUpsert: List<BudgetEntity>,
        associationsToDelete: List<BudgetAccountAssociation>,
        associationsToUpsert: List<BudgetAccountAssociation>
    ) {
        val timestamp = getCurrentTimestamp()
        val budgetsToSync = EntitiesToSync(
            toDelete = budgetsToDelete, toUpsert = budgetsToUpsert
        )
        val associationsToSync = EntitiesToSync(
            toDelete = associationsToDelete, toUpsert = associationsToUpsert
        )

        localSource.synchroniseBudgetsAndAssociations(
            budgetsToSync = budgetsToSync,
            associationsToSync = associationsToSync,
            timestamp = timestamp
        )
        userContext.getUserId()?.let { userId ->
            remoteSource.synchroniseBudgetsAndAssociations(
                budgetsToSync = budgetsToSync.map { deleted ->
                    toRemoteEntity(updateTime = timestamp, deleted = deleted)
                },
                associationsToSync = associationsToSync.map { deleted ->
                    toRemoteAssociation(updateTime = timestamp, deleted = deleted)
                },
                timestamp = timestamp,
                userId = userId
            )
        }
    }

    override suspend fun getBudgetAndAssociations(
        budgetId: Int
    ): Pair<BudgetEntity, List<BudgetAccountAssociation>>? {
        synchroniseBudgets()
        synchroniseBudgetAccountAssociations()

        val budget = localSource.getBudget(id = budgetId) ?: return null
        val associations = localSource.getBudgetAccountAssociations(budgetId = budgetId)

        return budget to associations
    }

    override suspend fun getAllBudgetsAndAssociations():
            Pair<List<BudgetEntity>, List<BudgetAccountAssociation>>
    {
        synchroniseBudgets()
        synchroniseBudgetAccountAssociations()

        val budgets = localSource.getAllBudgets()
        val associations = localSource.getAllBudgetAccountAssociations()

        return budgets to associations
    }

}