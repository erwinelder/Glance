package com.ataglance.walletglance.core.data.remote

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.WriteBatch
import kotlinx.coroutines.tasks.await

class FirestoreAdapterImpl <T> (
    private val firestore: FirebaseFirestore,
    private val collectionName: String,
    private val dataToEntityMapper: EntityMap.() -> T,
    private val entityToDataMapper: T.() -> EntityMap,
    private val getDocumentIdentifier: (T) -> String
) : FirestoreAdapter<T> {

    private fun getUserFirestoreRef(userId: String): DocumentReference {
        return firestore.collection("UserData").document(userId)
    }

    private fun getCollectionRef(userId: String): CollectionReference {
        return getUserFirestoreRef(userId).collection(collectionName)
    }

    private fun List<DocumentSnapshot>.toEntityList(): List<T> {
        return mapNotNull { it.data?.toMap()?.let(dataToEntityMapper) }
    }

    private fun WriteBatch.upsertEntities(entities: List<T>, userId: String) {
        entities.forEach { entity ->
            val documentRef = getCollectionRef(userId).document(getDocumentIdentifier(entity))
            val entityData = entityToDataMapper(entity)
            this.set(documentRef, entityData, SetOptions.merge())
        }
    }

    private fun WriteBatch.softDelete(documentRef: DocumentReference, timestamp: Long) {
        update(
            documentRef,
            mapOf("updateTime" to timestamp, "deleted" to true)
        )
    }


    override fun mapDataToEntity(data: EntityMap): T {
        return data.dataToEntityMapper()
    }


    override fun mapEntityToData(entity: T): EntityMap {
        return entity.entityToDataMapper()
    }


    override suspend fun upsertEntities(entities: List<T>, userId: String) {
        val batch = firestore.batch()
        batch.upsertEntities(entities = entities, userId = userId)
        batch.commit().await()
    }

    override suspend fun synchroniseEntities(toDelete: List<T>, toUpsert: List<T>, userId: String) {
        val batch = firestore.batch()

        batch.upsertEntities(entities = toDelete, userId = userId)
        batch.upsertEntities(entities = toUpsert, userId = userId)

        batch.commit().await()
    }


    @Deprecated("Unstable API")
    override suspend fun softDeleteAllEntities(timestamp: Long, userId: String) {
        softDeleteEntitiesInBatches(timestamp = timestamp, userId = userId)
    }

    @Deprecated("Unstable API")
    override suspend fun deleteAllEntities(userId: String) {
        deleteEntitiesInBatches(userId = userId)
    }

    private suspend fun softDeleteEntitiesInBatches(timestamp: Long, userId: String) {
        val batch = firestore.batch()

        val documentsToDelete = getCollectionRef(userId)
            .whereNotEqualTo("deleted", true)
            .limit(500)
            .get().await().documents

        documentsToDelete.forEach { document ->
            batch.softDelete(documentRef = document.reference, timestamp = timestamp)
        }
        batch.commit().await()

        if (documentsToDelete.size == 500) {
            softDeleteEntitiesInBatches(timestamp = timestamp, userId = userId)
        }
    }

    private suspend fun deleteEntitiesInBatches(userId: String) {
        val documentsToDelete = getCollectionRef(userId)
            .limit(500)
            .get().await().documents
        val batch = firestore.batch()

        documentsToDelete.forEach { document ->
            batch.delete(document.reference)
        }
        batch.commit().await()

        if (documentsToDelete.size == 500) {
            deleteEntitiesInBatches(userId = userId)
        }
    }


    override suspend fun getEntitiesAfterTimestamp(timestamp: Long, userId: String): List<T> {
        return getCollectionRef(userId)
            .whereNotEqualTo("deleted", true)
            .whereGreaterThan("updateTime", timestamp)
            .get().await().documents
            .toEntityList()
    }


    override suspend fun processCollectionDocumentsInBatch(
        userId: String,
        whereInField: String?,
        whereInValues: List<String>?,
        documentDataTransform: (EntityMap) -> EntityMap
    ): Int {
        val batch = firestore.batch()

        val querySnapshot = getCollectionRef(userId = userId)
            .whereNotEqualTo("deleted", true)
            .run {
                if (whereInField != null && whereInValues != null) {
                    whereIn(whereInField, whereInValues)
                } else {
                    this
                }
            }
            .limit(500)
            .get().await()

        querySnapshot.forEach { queryDocument ->
            queryDocument.data
                .let(documentDataTransform)
                .let { transformedData ->
                    batch.update(queryDocument.reference, transformedData)
                }
        }

        batch.commit().await()

        return querySnapshot.size()
    }

}