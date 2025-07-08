package com.ataglance.walletglance.navigation.data.repository

import com.ataglance.walletglance.core.data.model.DataSyncHelper
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.navigation.data.local.model.NavigationButtonEntity
import com.ataglance.walletglance.navigation.data.local.source.NavigationButtonLocalDataSource
import com.ataglance.walletglance.navigation.data.mapper.toDataModel
import com.ataglance.walletglance.navigation.data.mapper.toDto
import com.ataglance.walletglance.navigation.data.mapper.toEntity
import com.ataglance.walletglance.navigation.data.model.NavigationButtonDataModel
import com.ataglance.walletglance.navigation.data.remote.model.NavigationButtonDto
import com.ataglance.walletglance.navigation.data.remote.source.NavigationButtonRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class NavigationButtonRepositoryImpl(
    private val localSource: NavigationButtonLocalDataSource,
    private val remoteSource: NavigationButtonRemoteDataSource,
    private val syncHelper: DataSyncHelper
) : NavigationButtonRepository {

    private suspend fun synchronizeNavigationButtons() {
        syncHelper.synchronizeData(
            tableName = TableName.NavigationButton,
            localTimestampGetter = { localSource.getUpdateTime() },
            remoteTimestampGetter = { userId -> remoteSource.getUpdateTime(userId = userId) },
            localDataGetter = { timestamp ->
                localSource.getNavigationButtonsAfterTimestamp(timestamp = timestamp)
            },
            remoteDataGetter = { timestamp, userId ->
                remoteSource.getNavigationButtonsAfterTimestamp(timestamp = timestamp, userId = userId)
            },
            localHardCommand = { entitiesToDelete, entitiesToUpsert, timestamp ->
                localSource.deleteAndUpsertNavigationButtons(
                    toDelete = entitiesToDelete, toUpsert = entitiesToUpsert, timestamp = timestamp
                )
            },
            remoteSynchronizer = { data, timestamp, userId ->
                remoteSource.synchronizeNavigationButtons(
                    buttons = data, timestamp = timestamp, userId = userId
                )
            },
            entityDeletedPredicate = { it.deleted },
            entityToCommandDtoMapper = NavigationButtonEntity::toDto,
            queryDtoToEntityMapper = NavigationButtonDto::toEntity
        )
    }


    override suspend fun upsertNavigationButtons(buttons: List<NavigationButtonDataModel>) {
        syncHelper.upsertData(
            data = buttons,
            localTimestampGetter = { localSource.getUpdateTime() },
            remoteTimestampGetter = { userId -> remoteSource.getUpdateTime(userId = userId) },
            localSoftCommand = { entities, timestamp ->
                localSource.upsertNavigationButtons(buttons = entities, timestamp = timestamp)
                entities
            },
            remoteSoftCommand = { dtos, timestamp, userId ->
                remoteSource.synchronizeNavigationButtons(
                    buttons = dtos, timestamp = timestamp, userId = userId
                )
            },
            localDataAfterTimestampGetter = { timestamp ->
                localSource.getNavigationButtonsAfterTimestamp(timestamp = timestamp)
            },
            remoteSoftCommandAndDataAfterTimestampGetter = { dtos, timestamp, userId, localTimestamp ->
                remoteSource.synchronizeNavigationButtonsAndGetAfterTimestamp(
                    buttons = dtos,
                    timestamp = timestamp,
                    userId = userId,
                    localTimestamp = localTimestamp
                )
            },
            dataModelToEntityMapper = NavigationButtonDataModel::toEntity,
            dataModelToCommandDtoMapper = NavigationButtonDataModel::toDto,
            entityToCommandDtoMapper = NavigationButtonEntity::toDto,
            queryDtoToEntityMapper = NavigationButtonDto::toEntity
        )
    }

    override suspend fun deleteAllNavigationButtonsLocally() {
        localSource.deleteAllNavigationButtons()
    }

    override fun getAllNavigationButtonsAsFlow(): Flow<List<NavigationButtonDataModel>> {
        return localSource
            .getAllNavigationButtonsAsFlow()
            .onStart { synchronizeNavigationButtons() }
            .map { buttons ->
                buttons.map { it.toDataModel() }
            }
    }

}