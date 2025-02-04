package com.ataglance.walletglance.core.data.remote

import androidx.annotation.IntRange

typealias EntityMap = Map<String, Any?>

interface FirestoreAdapter <T> {

    fun mapDataToEntity(data: EntityMap): T

    fun mapEntityToData(entity: T): EntityMap


    suspend fun upsertEntities(entities: List<T>, userId: String)

    suspend fun synchroniseEntities(toDelete: List<T>, toUpsert: List<T>, userId: String)


    suspend fun softDeleteAllEntities(timestamp: Long, userId: String)

    suspend fun deleteAllEntities(userId: String)


    suspend fun getEntitiesAfterTimestamp(timestamp: Long, userId: String): List<T>


    suspend fun processCollectionDocumentsInBatch(
        userId: String,
        whereInField: String? = null,
        whereInValues: List<String>? = null,
        @IntRange(from = 1, to = 500) queryLimit: Long = 500,
        documentDataTransform: (EntityMap) -> EntityMap
    ): Int

}