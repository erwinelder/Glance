package com.ataglance.walletglance.core.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.ataglance.walletglance.core.data.model.TableUpdateTime
import kotlinx.coroutines.flow.Flow

@Dao
interface TableUpdateTimeDao {

    @Update
    suspend fun updateTime(entity: TableUpdateTime)

    @Query("SELECT timestamp FROM TableUpdateTime WHERE tableName = :tableName")
    fun getUpdateTime(tableName: String): Flow<Long>

}