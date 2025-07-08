package com.ataglance.walletglance.category.data.repository

import com.ataglance.walletglance.category.data.local.model.CategoryEntity
import com.ataglance.walletglance.category.data.local.source.CategoryLocalDataSource
import com.ataglance.walletglance.category.data.mapper.toCommandDto
import com.ataglance.walletglance.category.data.mapper.toDataModel
import com.ataglance.walletglance.category.data.mapper.toEntity
import com.ataglance.walletglance.category.data.model.CategoryDataModel
import com.ataglance.walletglance.category.data.remote.model.CategoryQueryDto
import com.ataglance.walletglance.category.data.remote.source.CategoryRemoteDataSource
import com.ataglance.walletglance.core.data.model.DataSyncHelper
import com.ataglance.walletglance.core.data.model.TableName
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class CategoryRepositoryImpl(
    val localSource: CategoryLocalDataSource,
    val remoteSource: CategoryRemoteDataSource,
    private val syncHelper: DataSyncHelper
) : CategoryRepository {

    private suspend fun synchronizeCategories() {
        syncHelper.synchronizeData(
            tableName = TableName.Category,
            localTimestampGetter = { localSource.getUpdateTime() },
            remoteTimestampGetter = { userId -> remoteSource.getUpdateTime(userId = userId) },
            localDataGetter = { timestamp ->
                localSource.getCategoriesAfterTimestamp(timestamp = timestamp)
            },
            remoteDataGetter = { timestamp, userId ->
                remoteSource.getCategoriesAfterTimestamp(timestamp = timestamp, userId = userId)
            },
            localHardCommand = { entitiesToDelete, entitiesToUpsert, timestamp ->
                localSource.deleteAndSaveCategories(
                    toDelete = entitiesToDelete, toUpsert = entitiesToUpsert, timestamp = timestamp
                )
            },
            remoteSynchronizer = { data, timestamp, userId ->
                remoteSource.synchronizeCategories(
                    categories = data, timestamp = timestamp, userId = userId
                )
            },
            entityDeletedPredicate = { it.deleted },
            entityToCommandDtoMapper = CategoryEntity::toCommandDto,
            queryDtoToEntityMapper = CategoryQueryDto::toEntity
        )
    }

    override suspend fun upsertCategories(categories: List<CategoryDataModel>) {
        syncHelper.upsertData(
            data = categories,
            localTimestampGetter = { localSource.getUpdateTime() },
            remoteTimestampGetter = { userId -> remoteSource.getUpdateTime(userId = userId) },
            localSoftCommand = { entities, timestamp ->
                localSource.saveCategories(categories = entities, timestamp = timestamp)
            },
            remoteSoftCommand = { dtos, timestamp, userId ->
                remoteSource.synchronizeCategories(
                    categories = dtos, timestamp = timestamp, userId = userId
                )
            },
            localDataAfterTimestampGetter = { timestamp ->
                localSource.getCategoriesAfterTimestamp(timestamp = timestamp)
            },
            remoteSoftCommandAndDataAfterTimestampGetter = { dtos, timestamp, userId, localTimestamp ->
                remoteSource.synchronizeCategoriesAndGetAfterTimestamp(
                    categories = dtos,
                    timestamp = timestamp,
                    userId = userId,
                    localTimestamp = localTimestamp
                )
            },
            dataModelToEntityMapper = CategoryDataModel::toEntity,
            dataModelToCommandDtoMapper = CategoryDataModel::toCommandDto,
            entityToCommandDtoMapper = CategoryEntity::toCommandDto,
            queryDtoToEntityMapper = CategoryQueryDto::toEntity
        )
    }

    override suspend fun deleteAndUpsertCategories(
        toDelete: List<CategoryDataModel>,
        toUpsert: List<CategoryDataModel>
    ) {
        syncHelper.deleteAndUpsertData(
            toDelete = toDelete,
            toUpsert = toUpsert,
            localTimestampGetter = { localSource.getUpdateTime() },
            remoteTimestampGetter = { userId -> remoteSource.getUpdateTime(userId = userId) },
            localSoftCommand = { entities, timestamp ->
                localSource.saveCategories(categories = entities, timestamp = timestamp)
            },
            localHardCommand = { entitiesToDelete, entitiesToUpsert, timestamp ->
                localSource.deleteAndSaveCategories(
                    toDelete = entitiesToDelete, toUpsert = entitiesToUpsert, timestamp = timestamp
                )
            },
            localDeleteCommand = { entities ->
                localSource.deleteCategories(categories = entities)
            },
            remoteSoftCommand = { dtos, timestamp, userId ->
                remoteSource.synchronizeCategories(
                    categories = dtos, timestamp = timestamp, userId = userId
                )
            },
            localDataAfterTimestampGetter = { timestamp ->
                localSource.getCategoriesAfterTimestamp(timestamp = timestamp)
            },
            remoteSoftCommandAndDataAfterTimestampGetter = { dtos, timestamp, userId, localTimestamp ->
                remoteSource.synchronizeCategoriesAndGetAfterTimestamp(
                    categories = dtos,
                    timestamp = timestamp,
                    userId = userId,
                    localTimestamp = localTimestamp
                )
            },
            entityDeletedPredicate = { it.deleted },
            dataModelToEntityMapper = CategoryDataModel::toEntity,
            dataModelToCommandDtoMapper = CategoryDataModel::toCommandDto,
            entityToCommandDtoMapper = CategoryEntity::toCommandDto,
            queryDtoToEntityMapper = CategoryQueryDto::toEntity
        )
    }

    override suspend fun deleteAllCategoriesLocally() {
        localSource.deleteAllCategories()
    }

    override fun getAllCategoriesAsFlow(): Flow<List<CategoryDataModel>> {
        return localSource
            .getAllCategoriesAsFlow()
            .onStart { synchronizeCategories() }
            .map { categories ->
                categories.map { it.toDataModel() }
            }
    }

    override suspend fun getAllCategories(): List<CategoryDataModel> {
        synchronizeCategories()
        return localSource.getAllCategories().map { it.toDataModel() }
    }

    override suspend fun getCategoriesByType(type: Char): List<CategoryDataModel> {
        synchronizeCategories()
        return localSource.getCategoriesByType(type = type).map { it.toDataModel() }
    }

}