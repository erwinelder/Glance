package com.ataglance.walletglance.core.data.local.source

import androidx.room.Transaction
import com.ataglance.walletglance.core.data.local.dao.BaseLocalDao
import com.ataglance.walletglance.core.data.local.dao.LocalUpdateTimeDao
import com.ataglance.walletglance.core.data.model.EntitiesToSync
import com.ataglance.walletglance.core.data.local.model.LocalUpdateTime
import com.ataglance.walletglance.core.data.model.TableName
import kotlinx.coroutines.flow.Flow

abstract class BaseLocalDataSource<T>(
    private val dao: BaseLocalDao<T>,
    private val updateTimeDao: LocalUpdateTimeDao,
    private val tableName: TableName
) {

    suspend fun updateLastModifiedTime(timestamp: Long) {
        updateTimeDao.updateTime(
            LocalUpdateTime(tableName.name, timestamp)
        )
    }

    suspend fun getLastModifiedTime(): Long? {
        return updateTimeDao.getUpdateTime(tableName.name)
    }

    @Transaction
    suspend fun upsertEntities(entityList: List<T>, timestamp: Long) {
        dao.upsertEntities(entityList)
        updateLastModifiedTime(timestamp)
    }

    @Transaction
    suspend fun deleteAndUpsertEntities(
        entitiesToDelete: List<T>,
        entitiesToUpsert: List<T>,
        timestamp: Long
    ) {
        if (entitiesToDelete.isNotEmpty()) {
            dao.deleteEntities(entitiesToDelete)
        }
        if (entitiesToUpsert.isNotEmpty()) {
            dao.upsertEntities(entitiesToUpsert)
        }
        updateLastModifiedTime(timestamp)
    }

    @Transaction
    suspend fun deleteAndUpsertEntities(
        entitiesToDeleteAndUpsert: EntitiesToSync<T>,
        timestamp: Long
    ) {
        if (entitiesToDeleteAndUpsert.toDelete.isNotEmpty()) {
            dao.deleteEntities(entitiesToDeleteAndUpsert.toDelete)
        }
        if (entitiesToDeleteAndUpsert.toUpsert.isNotEmpty()) {
            dao.upsertEntities(entitiesToDeleteAndUpsert.toUpsert)
        }
        updateLastModifiedTime(timestamp)
    }

    fun getAllEntities(): Flow<List<T>> = dao.getAllEntities()

}