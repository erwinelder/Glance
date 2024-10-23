package com.ataglance.walletglance.core.data.local

import androidx.room.Dao
import androidx.room.Update

@Dao
interface TableUpdateTimeDao {

    @Update
    suspend fun updateTime(tableName: String, timestamp: Long)

}