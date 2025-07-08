package com.ataglance.walletglance.budget.data.repository

import com.ataglance.walletglance.budget.data.local.model.BudgetOnWidgetEntity
import com.ataglance.walletglance.budget.data.local.source.BudgetOnWidgetLocalDataSource
import com.ataglance.walletglance.budget.data.mapper.budgetOnWidget.toDataModel
import com.ataglance.walletglance.budget.data.mapper.budgetOnWidget.toDto
import com.ataglance.walletglance.budget.data.mapper.budgetOnWidget.toEntity
import com.ataglance.walletglance.budget.data.model.BudgetOnWidgetDataModel
import com.ataglance.walletglance.budget.data.remote.model.BudgetOnWidgetDto
import com.ataglance.walletglance.budget.data.remote.source.BudgetOnWidgetRemoteDataSource
import com.ataglance.walletglance.core.data.model.DataSyncHelper
import com.ataglance.walletglance.core.data.model.TableName
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class BudgetOnWidgetRepositoryImpl(
    private val localSource: BudgetOnWidgetLocalDataSource,
    private val remoteSource: BudgetOnWidgetRemoteDataSource,
    private val syncHelper: DataSyncHelper
) : BudgetOnWidgetRepository {

    private suspend fun synchronizeBudgetsOnWidget() {
        syncHelper.synchronizeData(
            tableName = TableName.BudgetOnWidget,
            localTimestampGetter = { localSource.getUpdateTime() },
            remoteTimestampGetter = { userId -> remoteSource.getUpdateTime(userId = userId) },
            localDataGetter = { timestamp ->
                localSource.getBudgetsOnWidgetAfterTimestamp(timestamp = timestamp)
            },
            remoteDataGetter = { timestamp, userId ->
                remoteSource.getBudgetsOnWidgetAfterTimestamp(timestamp = timestamp, userId = userId)
            },
            localHardCommand = { entitiesToDelete, entitiesToUpsert, timestamp ->
                localSource.deleteAndUpsertBudgetsOnWidget(
                    toDelete = entitiesToDelete, toUpsert = entitiesToUpsert, timestamp = timestamp
                )
            },
            remoteSynchronizer = { data, timestamp, userId ->
                remoteSource.synchronizeBudgetsOnWidget(
                    budgets = data, timestamp = timestamp, userId = userId
                )
            },
            entityDeletedPredicate = { it.deleted },
            entityToCommandDtoMapper = BudgetOnWidgetEntity::toDto,
            queryDtoToEntityMapper = BudgetOnWidgetDto::toEntity
        )
    }

    override suspend fun deleteAndUpsertBudgetsOnWidget(
        toDelete: List<BudgetOnWidgetDataModel>,
        toUpsert: List<BudgetOnWidgetDataModel>
    ) {
        syncHelper.deleteAndUpsertData(
            toDelete = toDelete,
            toUpsert = toUpsert,
            localTimestampGetter = { localSource.getUpdateTime() },
            remoteTimestampGetter = { userId -> remoteSource.getUpdateTime(userId = userId) },
            localSoftCommand = { entities, timestamp ->
                localSource.upsertBudgetsOnWidget(budgets = entities, timestamp = timestamp)
            },
            localHardCommand = { entitiesToDelete, entitiesToUpsert, timestamp ->
                localSource.deleteAndUpsertBudgetsOnWidget(
                    toDelete = entitiesToDelete, toUpsert = entitiesToUpsert, timestamp = timestamp
                )
            },
            localDeleteCommand = { entities ->
                localSource.deleteBudgetsOnWidget(budgets = entities)
            },
            remoteSoftCommand = { dtos, timestamp, userId ->
                remoteSource.synchronizeBudgetsOnWidget(
                    budgets = dtos, timestamp = timestamp, userId = userId
                )
            },
            localDataAfterTimestampGetter = { timestamp ->
                localSource.getBudgetsOnWidgetAfterTimestamp(timestamp = timestamp)
            },
            remoteSoftCommandAndDataAfterTimestampGetter = { dtos, timestamp, userId, localTimestamp ->
                remoteSource.synchronizeBudgetsOnWidgetAndGetAfterTimestamp(
                    budgets = dtos,
                    timestamp = timestamp,
                    userId = userId,
                    localTimestamp = localTimestamp
                )
            },
            entityDeletedPredicate = { it.deleted },
            dataModelToEntityMapper = BudgetOnWidgetDataModel::toEntity,
            dataModelToCommandDtoMapper = BudgetOnWidgetDataModel::toDto,
            entityToCommandDtoMapper = BudgetOnWidgetEntity::toDto,
            queryDtoToEntityMapper = BudgetOnWidgetDto::toEntity
        )
    }

    override fun getAllBudgetsOnWidgetAsFlow(): Flow<List<BudgetOnWidgetDataModel>> {
        return localSource
            .getAllBudgetsOnWidgetAsFlow()
            .onStart { synchronizeBudgetsOnWidget() }
            .map { budgets ->
                budgets.map { it.toDataModel() }
            }
    }

    override suspend fun getAllBudgetsOnWidget(): List<BudgetOnWidgetDataModel> {
        synchronizeBudgetsOnWidget()
        return localSource.getAllBudgetsOnWidget().map { it.toDataModel() }
    }

}