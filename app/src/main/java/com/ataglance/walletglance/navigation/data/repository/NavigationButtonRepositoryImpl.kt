package com.ataglance.walletglance.navigation.data.repository

import com.ataglance.walletglance.core.data.model.DataSyncHelper
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.core.data.utils.synchroniseData
import com.ataglance.walletglance.core.utils.getCurrentTimestamp
import com.ataglance.walletglance.navigation.data.local.model.NavigationButtonEntity
import com.ataglance.walletglance.navigation.data.local.source.NavigationButtonLocalDataSource
import com.ataglance.walletglance.navigation.data.mapper.toLocalEntity
import com.ataglance.walletglance.navigation.data.mapper.toRemoteEntity
import com.ataglance.walletglance.navigation.data.remote.model.NavigationButtonRemoteEntity
import com.ataglance.walletglance.navigation.data.remote.source.NavigationButtonRemoteDataSource
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class NavigationButtonRepositoryImpl(
    private val localSource: NavigationButtonLocalDataSource,
    private val remoteSource: NavigationButtonRemoteDataSource,
    private val syncHelper: DataSyncHelper
) : NavigationButtonRepository {

    private suspend fun synchroniseNavigationButtons() {
        val userId = syncHelper.getUserIdForSync(TableName.NavigationButton) ?: return

        synchroniseData(
            localUpdateTimeGetter = localSource::getUpdateTime,
            remoteUpdateTimeGetter = { remoteSource.getUpdateTime(userId = userId) },
            remoteDataGetter = { timestamp ->
                remoteSource.getNavigationButtonsAfterTimestamp(timestamp = timestamp, userId = userId)
            },
            remoteDataToLocalDataMapper = NavigationButtonRemoteEntity::toLocalEntity,
            localSynchroniser = localSource::synchroniseNavigationButtons
        )
    }


    override suspend fun upsertNavigationButtons(buttons: List<NavigationButtonEntity>) {
        val timestamp = getCurrentTimestamp()

        localSource.upsertNavigationButtons(buttons = buttons, timestamp = timestamp)
        syncHelper.tryToSyncToRemote(TableName.NavigationButton) { userId ->
            remoteSource.upsertNavigationButtons(
                buttons = buttons.map {
                    it.toRemoteEntity(updateTime = timestamp, deleted = false)
                },
                timestamp = timestamp,
                userId = userId
            )
        }
    }

    override suspend fun deleteAllNavigationButtonsLocally() {
        val timestamp = getCurrentTimestamp()
        localSource.deleteAllNavigationButtons(timestamp = timestamp)
    }

    override fun getAllNavigationButtonsFlow(): Flow<List<NavigationButtonEntity>> = flow {
        coroutineScope {
            launch { synchroniseNavigationButtons() }
            localSource.getAllNavigationButtons().collect(::emit)
        }
    }

}