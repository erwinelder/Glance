package com.ataglance.walletglance.personalization.data.repository

import com.ataglance.walletglance.core.utils.getNowDateTimeLong
import com.ataglance.walletglance.personalization.data.local.WidgetLocalDataSource
import com.ataglance.walletglance.personalization.data.remote.WidgetRemoteDataSource

class WidgetRepositoryImpl(
    override val localSource: WidgetLocalDataSource,
    override val remoteSource: WidgetRemoteDataSource?
) : WidgetRepository {

    override suspend fun deleteAllEntities(
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ) {
        val timestamp = getNowDateTimeLong()
        localSource.deleteAllWidgets(timestamp)
        remoteSource?.deleteAllEntities(timestamp, onSuccessListener, onFailureListener)
    }

    override suspend fun deleteAllEntitiesLocally() {
        val timestamp = getNowDateTimeLong()
        localSource.deleteAllWidgets(timestamp = timestamp)
    }

}