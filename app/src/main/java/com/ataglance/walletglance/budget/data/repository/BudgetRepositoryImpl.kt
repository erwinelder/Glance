package com.ataglance.walletglance.budget.data.repository

import com.ataglance.walletglance.budget.data.local.model.BudgetEntityWithAssociations
import com.ataglance.walletglance.budget.data.local.source.BudgetLocalDataSource
import com.ataglance.walletglance.budget.data.mapper.budget.toDataModel
import com.ataglance.walletglance.budget.data.mapper.budget.toDataModelWithAssociations
import com.ataglance.walletglance.budget.data.mapper.budget.toDtoWithAssociations
import com.ataglance.walletglance.budget.data.mapper.budget.toEntityWithAssociations
import com.ataglance.walletglance.budget.data.mapper.budget.withAssociations
import com.ataglance.walletglance.budget.data.model.BudgetDataModel
import com.ataglance.walletglance.budget.data.model.BudgetDataModelWithAssociations
import com.ataglance.walletglance.budget.data.remote.model.BudgetDtoWithAssociations
import com.ataglance.walletglance.budget.data.remote.source.BudgetRemoteDataSource
import com.ataglance.walletglance.core.data.model.DataSyncHelper
import com.ataglance.walletglance.core.data.model.TableName

class BudgetRepositoryImpl(
    private val localSource: BudgetLocalDataSource,
    private val remoteSource: BudgetRemoteDataSource,
    private val syncHelper: DataSyncHelper
) : BudgetRepository {

    private suspend fun synchronizeBudgetsWithAssociations() {
        syncHelper.synchronizeData(
            tableName = TableName.Account,
            localTimestampGetter = { localSource.getUpdateTime() },
            remoteTimestampGetter = { userId -> remoteSource.getUpdateTime(userId = userId) },
            localDataGetter = { timestamp ->
                localSource.getBudgetsWithAssociationsAfterTimestamp(timestamp = timestamp)
            },
            remoteDataGetter = { timestamp, userId ->
                remoteSource.getBudgetsWithAssociationsAfterTimestamp(
                    timestamp = timestamp, userId = userId
                )
            },
            localHardCommand = { entitiesToDelete, entitiesToUpsert, timestamp ->
                localSource.deleteAndUpsertBudgetsWithAssociations(
                    toDelete = entitiesToDelete, toUpsert = entitiesToUpsert, timestamp = timestamp
                )
            },
            remoteSynchronizer = { data, timestamp, userId ->
                remoteSource.synchronizeBudgetsWithAssociations(
                    budgets = data, timestamp = timestamp, userId = userId
                )
            },
            entityDeletedPredicate = { it.deleted },
            entityToCommandDtoMapper = BudgetEntityWithAssociations::toDtoWithAssociations,
            queryDtoToEntityMapper = BudgetDtoWithAssociations::toEntityWithAssociations,
        )
    }

    override suspend fun deleteAndUpsertBudgetsWithAssociations(
        toDelete: List<BudgetDataModel>,
        toUpsert: List<BudgetDataModelWithAssociations>
    ) {
        syncHelper.deleteAndUpsertData(
            toDelete = toDelete.map { it.withAssociations() },
            toUpsert = toUpsert,
            localTimestampGetter = { localSource.getUpdateTime() },
            remoteTimestampGetter = { userId -> remoteSource.getUpdateTime(userId = userId) },
            localSoftCommand = { entities, timestamp ->
                localSource.upsertBudgetsWithAssociations(
                    budgetsWithAssociations = entities, timestamp = timestamp
                )
            },
            localHardCommand = { entitiesToDelete, entitiesToUpsert, timestamp ->
                localSource.deleteAndUpsertBudgetsWithAssociations(
                    toDelete = entitiesToDelete, toUpsert = entitiesToUpsert, timestamp = timestamp
                )
            },
            localDeleteCommand = { entities ->
                localSource.deleteBudgetsWithAssociations(budgetsWithAssociations = entities)
            },
            remoteSoftCommand = { dtos, timestamp, userId ->
                remoteSource.synchronizeBudgetsWithAssociations(
                    budgets = dtos, timestamp = timestamp, userId = userId
                )
            },
            localDataAfterTimestampGetter = { timestamp ->
                localSource.getBudgetsWithAssociationsAfterTimestamp(timestamp = timestamp)
            },
            remoteSoftCommandAndDataAfterTimestampGetter = { dtos, timestamp, userId, localTimestamp ->
                remoteSource.synchronizeBudgetsWithAssociationsAndGetAfterTimestamp(
                    budgets = dtos,
                    timestamp = timestamp,
                    userId = userId,
                    localTimestamp = localTimestamp
                )
            },
            entityDeletedPredicate = { it.deleted },
            dataModelToEntityMapper = BudgetDataModelWithAssociations::toEntityWithAssociations,
            dataModelToCommandDtoMapper = BudgetDataModelWithAssociations::toDtoWithAssociations,
            entityToCommandDtoMapper = BudgetEntityWithAssociations::toDtoWithAssociations,
            queryDtoToEntityMapper = BudgetDtoWithAssociations::toEntityWithAssociations
        )
    }

    override suspend fun getAllBudgets(): List<BudgetDataModel> {
        synchronizeBudgetsWithAssociations()
        return localSource.getAllBudgets().map { it.toDataModel() }
    }

    override suspend fun getBudgetWithAssociations(
        budgetId: Int
    ): BudgetDataModelWithAssociations? {
        synchronizeBudgetsWithAssociations()
        return localSource.getBudgetWithAssociations(budgetId = budgetId)
            ?.toDataModelWithAssociations()
    }

    override suspend fun getAllBudgetsWithAssociations(): List<BudgetDataModelWithAssociations> {
        synchronizeBudgetsWithAssociations()
        return localSource.getAllBudgetsWithAssociations().map { it.toDataModelWithAssociations() }
    }

}