package com.ataglance.walletglance.core.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TableUpdateTimeDao {

    @Update
    suspend fun updateTime(tableName: String, timestamp: Long)

    @Query("SELECT * FROM TableUpdateTime WHERE tableName = :tableName")
    fun getUpdateTime(tableName: String): Flow<Long>

}