package com.ataglance.walletglance.core.data.remote

import com.ataglance.walletglance.core.data.model.EntitiesToSynchronise
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.WriteBatch
import kotlinx.coroutines.tasks.await

private typealias EntityMap = Map<String, Any?>

class FirestoreAdapter <T> (
    private val firestore: FirebaseFirestore,
    private val collectionName: String,
    private val dataToEntityMapper: EntityMap.() -> T,
    private val entityToDataMapper: T.() -> EntityMap,
    private val getDocumentRef: CollectionReference.(T) -> DocumentReference
) {

    private fun getUserFirestoreRef(userId: String): DocumentReference {
        return firestore.collection("user_data").document(userId)
    }

    private fun getCollectionRef(userId: String): CollectionReference {
        return getUserFirestoreRef(userId).collection(collectionName)
    }


    private fun List<DocumentSnapshot>.toEntityList(): List<T> {
        return mapNotNull { it.data?.toMap()?.let(dataToEntityMapper) }
    }


    private fun WriteBatch.upsertEntities(entities: List<T>, userId: String) {
        entities.forEach { entity ->
            val documentRef = getCollectionRef(userId).getDocumentRef(entity)
            val entityData = entityToDataMapper(entity)
            this.set(documentRef, entityData, SetOptions.merge())
        }
    }

    private fun WriteBatch.softDelete(documentRef: DocumentReference, timestamp: Long) {
        update(
            documentRef,
            mapOf("updateTime" to timestamp, "isDeleted" to true)
        )
    }


    suspend fun upsertEntities(entities: List<T>, userId: String) {
        val batch = firestore.batch()
        batch.upsertEntities(entities = entities, userId = userId)
        batch.commit().await()
    }

    suspend fun synchroniseEntities(entitiesToSync: EntitiesToSynchronise<T>, userId: String) {
        val batch = firestore.batch()

        batch.upsertEntities(entities = entitiesToSync.toDelete, userId = userId)
        batch.upsertEntities(entities = entitiesToSync.toUpsert, userId = userId)

        batch.commit().await()
    }


    suspend fun softDeleteAllEntities(timestamp: Long, userId: String) {
        softDeleteEntitiesInBatches(timestamp = timestamp, userId = userId)
    }

    suspend fun deleteAllEntities(userId: String) {
        deleteEntitiesInBatches(userId = userId)
    }

    private suspend fun softDeleteEntitiesInBatches(timestamp: Long, userId: String) {
        val documentsToDelete = getCollectionRef(userId)
            .whereNotEqualTo("isDeleted", true)
            .limit(500)
            .get().await().documents
        val batch = firestore.batch()

        documentsToDelete.forEach { document ->
            batch.softDelete(documentRef = document.reference, timestamp = timestamp)
        }
        batch.commit().await()

        if (documentsToDelete.size >= 500) {
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

        if (documentsToDelete.size >= 500) {
            deleteEntitiesInBatches(userId = userId)
        }
    }


    suspend fun getEntitiesAfterTimestamp(timestamp: Long, userId: String): EntitiesToSynchronise<T> {
        return getCollectionRef(userId)
            .whereGreaterThan("updateTime", timestamp)
            .get().await().documents
            .partition { it.get("isDeleted") == true }
            .let { (deletedDocuments, updatedDocuments) ->
                EntitiesToSynchronise(
                    toUpsert = updatedDocuments.toEntityList(),
                    toDelete = deletedDocuments.toEntityList()
                )
            }
    }

}