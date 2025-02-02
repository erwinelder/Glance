package com.ataglance.walletglance.personalization.data.repository

import com.ataglance.walletglance.core.utils.getCurrentTimestamp
import com.ataglance.walletglance.personalization.data.local.WidgetLocalDataSource
import com.ataglance.walletglance.personalization.data.remote.WidgetRemoteDataSource

class WidgetRepositoryImpl(
    override val localSource: WidgetLocalDataSource,
    override val remoteSource: WidgetRemoteDataSource?
) : WidgetRepository {

    override suspend fun deleteAllEntities() {
        val timestamp = getCurrentTimestamp()
        localSource.deleteAllWidgets(timestamp = timestamp)
        remoteSource?.deleteAllEntities(timestamp = timestamp)
    }

    override suspend fun deleteAllEntitiesLocally() {
        val timestamp = getCurrentTimestamp()
        localSource.deleteAllWidgets(timestamp = timestamp)
    }

}