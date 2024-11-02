package com.ataglance.walletglance.core.data.remote

import com.ataglance.walletglance.core.data.model.EntitiesToUpsertAndDelete
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


    fun updateLastModifiedTime(timestamp: Long) {
        tableUpdateTimeCollectionRef.document(tableName.name)
            .set(mapOf("timestamp" to timestamp), SetOptions.merge())
    }

    fun getLastModifiedTime(): Long? {
        return tableUpdateTimeCollectionRef.document(tableName.name).get()
            .result?.get("timestamp") as? Long
    }

    fun WriteBatch.softDelete(documentRef: DocumentReference, timestamp: Long) {
        update(
            documentRef,
            mapOf("LMT" to timestamp, "isDeleted" to true)
        )
    }

    private fun List<DocumentSnapshot>.toEntityList(): List<T> {
        return mapNotNull { it.data?.toMap()?.let(dataToEntityMapper) }
    }

    fun upsertEntities(
        entityList: List<T>,
        timestamp: Long,
        onSuccessListener: () -> Unit = {},
        onFailureListener: (Exception) -> Unit = {}
    ) {
        entityList.forEach { entity ->
            val entityData = entityToDataMapper(entity, timestamp)

            collectionRef.getDocumentRef(entity)
                .set(entityData, SetOptions.merge())
                .addOnSuccessListener {
                    updateLastModifiedTime(timestamp)
                    onSuccessListener()
                }
                .addOnFailureListener(onFailureListener)
        }
    }

    fun deleteAndUpsertEntities(
        entitiesToDelete: List<T>,
        entitiesToUpsert: List<T>,
        timestamp: Long,
        onSuccessListener: () -> Unit = {},
        onFailureListener: (Exception) -> Unit = {}
    ) {
        val batch = firestore.batch()

        entitiesToDelete.forEach { entity ->
            batch.softDelete(collectionRef.getDocumentRef(entity), timestamp)
        }

        entitiesToUpsert.forEach { entity ->
            val entityData = entityToDataMapper(entity, timestamp)
            batch.set(collectionRef.getDocumentRef(entity), entityData, SetOptions.merge())
        }

        batch.commit()
            .addOnSuccessListener {
                updateLastModifiedTime(timestamp)
                onSuccessListener()
            }
            .addOnFailureListener(onFailureListener)
    }

    fun deleteAllEntities(
        timestamp: Long,
        onSuccessListener: () -> Unit = {},
        onFailureListener: (Exception) -> Unit = {}
    ) {
        deleteEntitiesInBatches(
            timestamp = timestamp,
            onSuccessListener = onSuccessListener,
            onFailureListener = onFailureListener
        )
    }

    private fun deleteEntitiesInBatches(
        timestamp: Long,
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ) {
        collectionRef.limit(500).get()
            .addOnSuccessListener { querySnapshot ->
                val batch = firestore.batch()

                querySnapshot.documents.forEach { document ->
                    batch.softDelete(document.reference, timestamp)
                }

                batch.commit()
                    .addOnSuccessListener {
                        if (querySnapshot.size() == 500) {
                            deleteEntitiesInBatches(
                                timestamp = timestamp,
                                onSuccessListener = onSuccessListener,
                                onFailureListener = onFailureListener
                            )
                        } else {
                            onSuccessListener()
                        }
                        updateLastModifiedTime(timestamp)
                    }
                    .addOnFailureListener(onFailureListener)
            }
            .addOnFailureListener(onFailureListener)
    }

    fun getEntitiesAfterTimestamp(
        timestamp: Long
    ): Flow<EntitiesToUpsertAndDelete<T>> = callbackFlow {
        val query = collectionRef.whereGreaterThan("LMT", timestamp)

        val listenerRegistration = query.addSnapshotListener { querySnapshot, exception ->
            if (exception != null) {
                close(exception)
                return@addSnapshotListener
            }

            val entitiesToUpsertAndDelete = querySnapshot?.documents
                ?.partition { it.get("isDeleted") == true }
                ?.let { (deletedDocuments, updatedDocuments) ->
                    EntitiesToUpsertAndDelete(
                        toUpsert = updatedDocuments.toEntityList(),
                        toDelete = deletedDocuments.toEntityList()
                    )
                }
                ?: EntitiesToUpsertAndDelete()

            trySend(entitiesToUpsertAndDelete)
        }

        awaitClose { listenerRegistration.remove() }
    }

}