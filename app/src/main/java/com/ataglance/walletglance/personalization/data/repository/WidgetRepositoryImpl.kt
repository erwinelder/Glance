package com.ataglance.walletglance.personalization.data.repository

import com.ataglance.walletglance.auth.data.model.UserContext
import com.ataglance.walletglance.core.data.model.EntitiesToSync
import com.ataglance.walletglance.core.data.utils.synchroniseData
import com.ataglance.walletglance.core.utils.getCurrentTimestamp
import com.ataglance.walletglance.personalization.data.local.model.WidgetEntity
import com.ataglance.walletglance.personalization.data.local.source.WidgetLocalDataSource
import com.ataglance.walletglance.personalization.data.mapper.toLocalEntity
import com.ataglance.walletglance.personalization.data.mapper.toRemoteEntity
import com.ataglance.walletglance.personalization.data.remote.model.WidgetRemoteEntity
import com.ataglance.walletglance.personalization.data.remote.source.WidgetRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WidgetRepositoryImpl(
    private val localSource: WidgetLocalDataSource,
    private val remoteSource: WidgetRemoteDataSource,
    private val userContext: UserContext
) : WidgetRepository {

    private suspend fun synchroniseWidgets() {
        val userId = userContext.getUserId() ?: return

        synchroniseData(
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
        userContext.getUserId()?.let { userId ->
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
        userContext.getUserId()?.let { userId ->
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

    override fun getAllWidgets(): Flow<List<WidgetEntity>> = flow {
        synchroniseWidgets()
        localSource.getAllWidgets().collect(::emit)
    }

}