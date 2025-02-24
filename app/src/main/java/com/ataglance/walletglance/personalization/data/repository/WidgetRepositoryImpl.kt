package com.ataglance.walletglance.personalization.data.repository

import com.ataglance.walletglance.core.data.model.DataSyncHelper
import com.ataglance.walletglance.core.data.model.EntitiesToSync
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.core.data.utils.synchroniseDataFromRemote
import com.ataglance.walletglance.core.utils.getCurrentTimestamp
import com.ataglance.walletglance.personalization.data.local.model.WidgetEntity
import com.ataglance.walletglance.personalization.data.local.source.WidgetLocalDataSource
import com.ataglance.walletglance.personalization.data.mapper.toLocalEntity
import com.ataglance.walletglance.personalization.data.mapper.toRemoteEntity
import com.ataglance.walletglance.personalization.data.remote.model.WidgetRemoteEntity
import com.ataglance.walletglance.personalization.data.remote.source.WidgetRemoteDataSource
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class WidgetRepositoryImpl(
    private val localSource: WidgetLocalDataSource,
    private val remoteSource: WidgetRemoteDataSource,
    private val syncHelper: DataSyncHelper
) : WidgetRepository {

    private suspend fun synchroniseWidgets() {
        val userId = syncHelper.getUserIdForSync(TableName.Widget) ?: return

        synchroniseDataFromRemote(
            localUpdateTimeGetter = localSource::getUpdateTime,
            remoteUpdateTimeGetter = { remoteSource.getUpdateTime(userId = userId) },
            remoteDataGetter = { timestamp ->
                remoteSource.getWidgetsAfterTimestamp(timestamp = timestamp, userId = userId)
            },
            remoteDataToLocalDataMapper = WidgetRemoteEntity::toLocalEntity,
            localSynchroniser = localSource::synchroniseWidgets
        )
    }


    override suspend fun upsertWidgets(widgets: List<WidgetEntity>) {
        val timestamp = getCurrentTimestamp()

        localSource.upsertWidgets(widgets = widgets, timestamp = timestamp)
        syncHelper.tryToSyncToRemote(TableName.Widget) { userId ->
            remoteSource.upsertWidgets(
                widgets = widgets.map {
                    it.toRemoteEntity(updateTime = timestamp, deleted = false)
                },
                timestamp = timestamp,
                userId = userId
            )
        }
    }

    override suspend fun deleteAndUpsertWidgets(
        toDelete: List<WidgetEntity>,
        toUpsert: List<WidgetEntity>
    ) {
        val timestamp = getCurrentTimestamp()
        val widgetsToSync = EntitiesToSync(toDelete = toDelete, toUpsert = toUpsert)

        localSource.synchroniseWidgets(widgetsToSync = widgetsToSync, timestamp = timestamp)
        syncHelper.tryToSyncToRemote(TableName.Widget) { userId ->
            remoteSource.synchroniseWidgets(
                widgetsToSync = widgetsToSync.map { deleted ->
                    toRemoteEntity(updateTime = timestamp, deleted = deleted)
                },
                timestamp = timestamp,
                userId = userId
            )
        }
    }

    override suspend fun deleteAllWidgetsLocally() {
        val timestamp = getCurrentTimestamp()
        localSource.deleteAllWidgets(timestamp = timestamp)
    }

    override fun getAllWidgetsFlow(): Flow<List<WidgetEntity>> = flow {
        coroutineScope {
            launch { synchroniseWidgets() }
            localSource.getAllWidgets().collect(::emit)
        }
    }

    override suspend fun getAllWidgets(): List<WidgetEntity> {
        synchroniseWidgets()
        return localSource.getAllWidgets().firstOrNull().orEmpty()
    }

}