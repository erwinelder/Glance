package com.ataglance.walletglance.personalization.data.repository

import com.ataglance.walletglance.core.data.model.DataSyncHelper
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.personalization.data.local.model.WidgetEntity
import com.ataglance.walletglance.personalization.data.local.source.WidgetLocalDataSource
import com.ataglance.walletglance.personalization.data.mapper.toDataModel
import com.ataglance.walletglance.personalization.data.mapper.toDto
import com.ataglance.walletglance.personalization.data.mapper.toEntity
import com.ataglance.walletglance.personalization.data.model.WidgetDataModel
import com.ataglance.walletglance.personalization.data.remote.model.WidgetDto
import com.ataglance.walletglance.personalization.data.remote.source.WidgetRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class WidgetRepositoryImpl(
    private val localSource: WidgetLocalDataSource,
    private val remoteSource: WidgetRemoteDataSource,
    private val syncHelper: DataSyncHelper
) : WidgetRepository {

    private suspend fun synchronizeWidgets() {
        syncHelper.synchronizeData(
            tableName = TableName.Widget,
            localTimestampGetter = { localSource.getUpdateTime() },
            remoteTimestampGetter = { userId -> remoteSource.getUpdateTime(userId = userId) },
            localDataGetter = { timestamp ->
                localSource.getWidgetsAfterTimestamp(timestamp = timestamp)
            },
            remoteDataGetter = { timestamp, userId ->
                remoteSource.getWidgetsAfterTimestamp(timestamp = timestamp, userId = userId)
            },
            localHardCommand = { entitiesToDelete, entitiesToUpsert, timestamp ->
                localSource.deleteAndUpsertWidgets(
                    toDelete = entitiesToDelete, toUpsert = entitiesToUpsert, timestamp = timestamp
                )
            },
            remoteSynchronizer = { data, timestamp, userId ->
                remoteSource.synchronizeWidgets(
                    widgets = data, timestamp = timestamp, userId = userId
                )
            },
            entityDeletedPredicate = { it.deleted },
            entityToCommandDtoMapper = WidgetEntity::toDto,
            queryDtoToEntityMapper = WidgetDto::toEntity
        )
    }


    override suspend fun upsertWidgets(widgets: List<WidgetDataModel>) {
        syncHelper.upsertData(
            data = widgets,
            localTimestampGetter = { localSource.getUpdateTime() },
            remoteTimestampGetter = { userId -> remoteSource.getUpdateTime(userId = userId) },
            localSoftCommand = { entities, timestamp ->
                localSource.upsertWidgets(widgets = entities, timestamp = timestamp)
                entities
            },
            remoteSoftCommand = { dtos, timestamp, userId ->
                remoteSource.synchronizeWidgets(
                    widgets = dtos, timestamp = timestamp, userId = userId
                )
            },
            localDataAfterTimestampGetter = { timestamp ->
                localSource.getWidgetsAfterTimestamp(timestamp = timestamp)
            },
            remoteSoftCommandAndDataAfterTimestampGetter = { dtos, timestamp, userId, localTimestamp ->
                remoteSource.synchronizeWidgetsAndGetAfterTimestamp(
                    widgets = dtos,
                    timestamp = timestamp,
                    userId = userId,
                    localTimestamp = localTimestamp
                )
            },
            dataModelToEntityMapper = WidgetDataModel::toEntity,
            dataModelToCommandDtoMapper = WidgetDataModel::toDto,
            entityToCommandDtoMapper = WidgetEntity::toDto,
            queryDtoToEntityMapper = WidgetDto::toEntity
        )
    }

    override suspend fun deleteAndUpsertWidgets(
        toDelete: List<WidgetDataModel>,
        toUpsert: List<WidgetDataModel>
    ) {
        syncHelper.deleteAndUpsertData(
            toDelete = toDelete,
            toUpsert = toUpsert,
            localTimestampGetter = { localSource.getUpdateTime() },
            remoteTimestampGetter = { userId -> remoteSource.getUpdateTime(userId = userId) },
            localSoftCommand = { entities, timestamp ->
                localSource.upsertWidgets(widgets = entities, timestamp = timestamp)
            },
            localHardCommand = { entitiesToDelete, entitiesToUpsert, timestamp ->
                localSource.deleteAndUpsertWidgets(
                    toDelete = entitiesToDelete, toUpsert = entitiesToUpsert, timestamp = timestamp
                )
            },
            localDeleteCommand = { entities ->
                localSource.deleteWidgets(widgets = entities)
            },
            remoteSoftCommand = { dtos, timestamp, userId ->
                remoteSource.synchronizeWidgets(
                    widgets = dtos, timestamp = timestamp, userId = userId
                )
            },
            localDataAfterTimestampGetter = { timestamp ->
                localSource.getWidgetsAfterTimestamp(timestamp = timestamp)
            },
            remoteSoftCommandAndDataAfterTimestampGetter = { dtos, timestamp, userId, localTimestamp ->
                remoteSource.synchronizeWidgetsAndGetAfterTimestamp(
                    widgets = dtos,
                    timestamp = timestamp,
                    userId = userId,
                    localTimestamp = localTimestamp
                )
            },
            entityDeletedPredicate = { it.deleted },
            dataModelToEntityMapper = WidgetDataModel::toEntity,
            dataModelToCommandDtoMapper = WidgetDataModel::toDto,
            entityToCommandDtoMapper = WidgetEntity::toDto,
            queryDtoToEntityMapper = WidgetDto::toEntity
        )
    }

    override suspend fun deleteAllWidgetsLocally() {
        localSource.deleteAllWidgets()
    }

    override fun getAllWidgetsAsFlow(): Flow<List<WidgetDataModel>> {
        return localSource
            .getAllWidgetsAsFlow()
            .onStart { synchronizeWidgets() }
            .map { widgets ->
                widgets.map { it.toDataModel() }
            }
    }

    override suspend fun getAllWidgets(): List<WidgetDataModel> {
        synchronizeWidgets()
        return localSource.getAllWidgets().map { it.toDataModel() }
    }

}