package com.ataglance.walletglance.core.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface BaseLocalDao<T> {

    @Upsert
    suspend fun upsertEntities(entities: List<T>)

    @Delete
    suspend fun deleteEntities(entities: List<T>)

    @Transaction
    suspend fun deleteAndUpsertEntities(toDelete: List<T>, toUpsert: List<T>) {
        deleteEntities(toDelete)
        upsertEntities(toUpsert)
    }

    @Deprecated("Use custom implementation instead")
    fun getAllEntities(): Flow<List<T>>

}