package com.ataglance.walletglance.core.data.remote

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

abstract class BaseRemoteDataSource<T>(
    private val userId: String,
    private val firestore: FirebaseFirestore,
    private val collectionName: String,

    private val getDocumentRef: CollectionReference.(T) -> DocumentReference,
    private val dataToEntityMapper: (Map<String, Any>) -> T,
    private val entityToDataMapper: (T) -> Map<String, Any>
) {

    val collectionRef
        get() = firestore.collection("users").document(userId).collection(collectionName)

    fun upsertEntities(
        entityList: List<T>,
        onSuccessListener: () -> Unit = {},
        onFailureListener: (Exception) -> Unit = {}
    ) {
        entityList.forEach { entity ->
            val entityData = entityToDataMapper(entity)
            collectionRef.getDocumentRef(entity)
                .set(entityData, SetOptions.merge())
                .addOnSuccessListener { onSuccessListener() }
                .addOnFailureListener(onFailureListener)
        }
    }

    fun deleteAllEntities(
        onSuccessListener: () -> Unit = {},
        onFailureListener: (Exception) -> Unit = {}
    ) {
        deleteEntitiesInBatches(
            collectionRef = collectionRef,
            onSuccessListener = onSuccessListener,
            onFailureListener = onFailureListener
        )
    }

    private fun deleteEntitiesInBatches(
        collectionRef: CollectionReference,
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
                                collectionRef = collectionRef,
                                onSuccessListener = onSuccessListener,
                                onFailureListener = onFailureListener
                            )
                        } else {
                            onSuccessListener()
                        }
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