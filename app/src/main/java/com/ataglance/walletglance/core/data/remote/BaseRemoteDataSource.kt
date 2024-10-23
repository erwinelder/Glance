package com.ataglance.walletglance.core.data.remote

import com.ataglance.walletglance.core.data.model.TableName
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

abstract class BaseRemoteDataSource<T>(
    private val userId: String,
    val firestore: FirebaseFirestore,
    private val collectionName: String,
    private val tableName: TableName,

    private val getDocumentRef: CollectionReference.(T) -> DocumentReference,
    private val dataToEntityMapper: (Map<String, Any>) -> T,
    private val entityToDataMapper: (T) -> Map<String, Any>
) {

    private val userFirestoreRef
        get() = firestore.collection("users").document(userId)

    val collectionRef
        get() = userFirestoreRef.collection(collectionName)

    private val tableUpdateTimeCollectionRef
        get() = userFirestoreRef.collection("tableUpdateTimes")

    fun tableUpdateTimeCollectionRef(timestamp: Long) {
        tableUpdateTimeCollectionRef.document(tableName.name)
            .set(mapOf("timestamp" to timestamp), SetOptions.merge())
    }

    fun upsertEntities(
        entityList: List<T>,
        timestamp: Long,
        onSuccessListener: () -> Unit = {},
        onFailureListener: (Exception) -> Unit = {}
    ) {
        entityList.forEach { entity ->
            val entityData = entityToDataMapper(entity)
            collectionRef.getDocumentRef(entity)
                .set(entityData, SetOptions.merge())
                .addOnSuccessListener {
                    tableUpdateTimeCollectionRef(timestamp)
                    onSuccessListener()
                }
                .addOnFailureListener(onFailureListener)
        }
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
                    batch.delete(document.reference)
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
                        tableUpdateTimeCollectionRef(timestamp)
                    }
                    .addOnFailureListener(onFailureListener)
            }
            .addOnFailureListener(onFailureListener)
    }

    fun getAllEntities(
        onSuccessListener: (List<T>) -> Unit,
        onFailureListener: (Exception) -> Unit = {}
    ) {
        collectionRef.get()
            .addOnSuccessListener { querySnapshot ->
                val accountList = querySnapshot.documents.mapNotNull {
                    it.data?.toMap()?.let(dataToEntityMapper) ?: return@mapNotNull null
                }
                onSuccessListener(accountList)
            }
            .addOnFailureListener(onFailureListener)
    }

}