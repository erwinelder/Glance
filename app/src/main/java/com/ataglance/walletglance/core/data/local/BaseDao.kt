package com.ataglance.walletglance.core.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Upsert

@Dao
interface BaseDao<T> {

    @Upsert
    suspend fun upsertEntities(entities: List<T>)

    @Delete
    suspend fun deleteEntities(entities: List<T>)

}