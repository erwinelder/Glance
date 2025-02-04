package com.ataglance.walletglance.core.data.remote

import com.ataglance.walletglance.core.data.model.EntitiesToSync
import com.ataglance.walletglance.core.data.model.TableName
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.WriteBatch
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

abstract class BaseRemoteDataSource<T>(
    private val userId: String,
    val firestore: FirebaseFirestore,
    private val collectionName: String,
    private val tableName: TableName,

    private val getDocumentRef: CollectionReference.(T) -> DocumentReference,
    private val dataToEntityMapper: (Map<String, Any?>) -> T,
    private val entityToDataMapper: T.(Long) -> Map<String, Any?>
) {

    private val userFirestoreRef
        get() = firestore.collection("usersData").document(userId)

    val collectionRef
        get() = userFirestoreRef.collection(collectionName)

    private val tableUpdateTimeCollectionRef
        get() = userFirestoreRef.collection("tableUpdateTimes")


    suspend fun updateLastModifiedTime(timestamp: Long) {
        tableUpdateTimeCollectionRef.document(tableName.name)
            .set(mapOf("timestamp" to timestamp), SetOptions.merge())
            .await()
    }

    suspend fun getLastModifiedTime(): Long? {
        val updateTime = tableUpdateTimeCollectionRef.document(tableName.name).get().await()
            ?.get("timestamp") as? Long
        return updateTime
    }

    private fun WriteBatch.softDelete(documentRef: DocumentReference, timestamp: Long) {
        update(
            documentRef,
            mapOf("LMT" to timestamp, "isDeleted" to true)
        )
    }

    private fun List<DocumentSnapshot>.toEntityList(): List<T> {
        return mapNotNull { it.data?.toMap()?.let(dataToEntityMapper) }
    }

    suspend fun upsertEntities(entityList: List<T>, timestamp: Long) {
        entityList.forEach { entity ->
            val entityData = entityToDataMapper(entity, timestamp)

            collectionRef.getDocumentRef(entity).set(entityData, SetOptions.merge()).await()
            updateLastModifiedTime(timestamp)
        }
    }

    suspend fun deleteAndUpsertEntities(
        entitiesToDelete: List<T>,
        entitiesToUpsert: List<T>,
        timestamp: Long
    ) {
        val batch = firestore.batch()

        entitiesToDelete.forEach { entity ->
            batch.softDelete(collectionRef.getDocumentRef(entity), timestamp)
        }

        entitiesToUpsert.forEach { entity ->
            val entityData = entityToDataMapper(entity, timestamp)
            batch.set(collectionRef.getDocumentRef(entity), entityData, SetOptions.merge())
        }

        batch.commit().await()
        updateLastModifiedTime(timestamp)
    }

    suspend fun deleteAllEntities(timestamp: Long) {
        deleteEntitiesInBatches(timestamp = timestamp)
    }

    private suspend fun deleteEntitiesInBatches(timestamp: Long) {
        val querySnapshot = collectionRef.limit(500).get().await()
        val batch = firestore.batch()

        querySnapshot.documents.forEach { document ->
            batch.softDelete(document.reference, timestamp)
        }
        batch.commit()

        if (querySnapshot.size() == 500) {
            deleteEntitiesInBatches(timestamp = timestamp)
        } else {
            updateLastModifiedTime(timestamp)
        }
    }

    fun getEntitiesAfterTimestamp(
        timestamp: Long
    ): Flow<EntitiesToSync<T>> = callbackFlow {
        val query = collectionRef.whereGreaterThan("LMT", timestamp)

        val listenerRegistration = query.addSnapshotListener { querySnapshot, exception ->
            if (exception != null) {
                close(exception)
                return@addSnapshotListener
            }

            val entitiesToUpsertAndDelete = querySnapshot?.documents
                ?.partition { it.get("isDeleted") == true }
                ?.let { (deletedDocuments, updatedDocuments) ->
                    EntitiesToSync(
                        toUpsert = updatedDocuments.toEntityList(),
                        toDelete = deletedDocuments.toEntityList()
                    )
                }
                ?: EntitiesToSync()

            trySend(entitiesToUpsertAndDelete)
        }

        awaitClose { listenerRegistration.remove() }
    }

}