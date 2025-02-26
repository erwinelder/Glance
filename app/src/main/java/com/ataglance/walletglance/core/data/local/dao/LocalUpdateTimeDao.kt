package com.ataglance.walletglance.core.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.ataglance.walletglance.core.data.local.model.LocalUpdateTime

@Dao
interface LocalUpdateTimeDao {

    @Upsert
    suspend fun updateTime(entity: LocalUpdateTime)

    suspend fun saveUpdateTime(tableName: String, timestamp: Long) {
        updateTime(
            entity = LocalUpdateTime(tableName = tableName, timestamp = timestamp)
        )
    }

    @Query("SELECT timestamp FROM local_update_time WHERE tableName = :tableName")
    suspend fun getUpdateTime(tableName: String): Long?

}