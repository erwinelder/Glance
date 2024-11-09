package com.ataglance.walletglance.core.data.local

import androidx.room.Transaction
import com.ataglance.walletglance.core.data.model.EntitiesToUpsertAndDelete
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.core.data.model.TableUpdateTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

abstract class BaseLocalDataSource<T>(
    private val dao: BaseDao<T>,
    private val updateTimeDao: TableUpdateTimeDao,
    private val tableName: TableName
) {

    suspend fun updateLastModifiedTime(timestamp: Long) {
        updateTimeDao.updateTime(
            TableUpdateTime(tableName.name, timestamp)
        )
    }

    suspend fun getLastModifiedTime(): Long {
        return updateTimeDao.getUpdateTime(tableName.name).first()
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
        entitiesToDeleteAndUpsert: EntitiesToUpsertAndDelete<T>,
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