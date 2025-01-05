package com.ataglance.walletglance.core.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.ataglance.walletglance.core.data.model.TableUpdateTime

@Dao
interface TableUpdateTimeDao {

    @Upsert
    suspend fun updateTime(entity: TableUpdateTime)

    @Query("SELECT timestamp FROM TableUpdateTime WHERE tableName = :tableName")
    suspend fun getUpdateTime(tableName: String): Long?

}