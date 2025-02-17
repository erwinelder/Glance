package com.ataglance.walletglance.category.data.repository

import com.ataglance.walletglance.category.data.local.model.CategoryEntity
import com.ataglance.walletglance.category.data.local.source.CategoryLocalDataSource
import com.ataglance.walletglance.category.data.mapper.toLocalEntity
import com.ataglance.walletglance.category.data.mapper.toRemoteEntity
import com.ataglance.walletglance.category.data.remote.model.CategoryRemoteEntity
import com.ataglance.walletglance.category.data.remote.source.CategoryRemoteDataSource
import com.ataglance.walletglance.core.data.model.DataSyncHelper
import com.ataglance.walletglance.core.data.model.EntitiesToSync
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.core.data.utils.synchroniseData
import com.ataglance.walletglance.core.utils.getCurrentTimestamp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CategoryRepositoryImpl(
    val localSource: CategoryLocalDataSource,
    val remoteSource: CategoryRemoteDataSource,
    private val syncHelper: DataSyncHelper
) : CategoryRepository {

    private suspend fun synchroniseCategories() {
        val userId = syncHelper.getUserIdForSync(TableName.Category) ?: return

        synchroniseData(
            localUpdateTimeGetter = localSource::getUpdateTime,
            remoteUpdateTimeGetter = { remoteSource.getUpdateTime(userId = userId) },
            remoteDataGetter = { timestamp ->
                remoteSource.getCategoriesAfterTimestamp(timestamp = timestamp, userId = userId)
            },
            remoteDataToLocalDataMapper = CategoryRemoteEntity::toLocalEntity,
            localSynchroniser = localSource::synchroniseCategories
        )
    }

    override suspend fun upsertCategories(categories: List<CategoryEntity>) {
        val timestamp = getCurrentTimestamp()

        localSource.upsertCategories(categories = categories, timestamp = timestamp)
        syncHelper.tryToSyncToRemote(TableName.Category) { userId ->
            remoteSource.upsertCategories(
                categories = categories.map {
                    it.toRemoteEntity(updateTime = timestamp, deleted = false)
                },
                timestamp = timestamp,
                userId = userId
            )
        }
    }

    override suspend fun deleteAndUpsertCategories(
        toDelete: List<CategoryEntity>,
        toUpsert: List<CategoryEntity>
    ) {
        val timestamp = getCurrentTimestamp()
        val categoriesToSync = EntitiesToSync(toDelete = toDelete, toUpsert = toUpsert)

        localSource.synchroniseCategories(categoriesToSync = categoriesToSync, timestamp = timestamp)
        syncHelper.tryToSyncToRemote(TableName.Category) { userId ->
            remoteSource.synchroniseCategories(
                categoriesToSync = categoriesToSync.map { deleted ->
                    toRemoteEntity(updateTime = timestamp, deleted = deleted)
                },
                timestamp = timestamp,
                userId = userId
            )
        }
    }

    override suspend fun deleteAllCategoriesLocally() {
        val timestamp = getCurrentTimestamp()
        localSource.deleteAllCategories(timestamp = timestamp)
    }

    override fun getAllCategories(): Flow<List<CategoryEntity>> = flow {
        synchroniseCategories()
        localSource.getAllCategories().collect(::emit)
    }

    override suspend fun getCategoriesByType(type: Char): List<CategoryEntity> {
        synchroniseCategories()
        return localSource.getCategoriesByType(type = type)
    }

}