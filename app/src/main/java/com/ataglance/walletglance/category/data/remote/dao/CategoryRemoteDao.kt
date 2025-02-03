package com.ataglance.walletglance.category.data.remote.dao

import com.ataglance.walletglance.category.data.mapper.toCategoryRemoteEntity
import com.ataglance.walletglance.category.data.mapper.toMap
import com.ataglance.walletglance.category.data.remote.model.CategoryRemoteEntity
import com.ataglance.walletglance.core.data.model.EntitiesToSynchronise
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.core.data.remote.FirestoreAdapter
import com.google.firebase.firestore.FirebaseFirestore

class CategoryRemoteDao(
    private val firestoreAdapter: FirestoreAdapter<CategoryRemoteEntity>
) {

    suspend fun upsertEntities(entities: List<CategoryRemoteEntity>, userId: String) {
        firestoreAdapter.upsertEntities(entities = entities, userId = userId)
    }

    suspend fun synchroniseEntities(
        entitiesToSync: EntitiesToSynchronise<CategoryRemoteEntity>,
        userId: String
    ) {
        firestoreAdapter.synchroniseEntities(entitiesToSync = entitiesToSync, userId = userId)
    }

    suspend fun getEntitiesAfterTimestamp(
        timestamp: Long,
        userId: String
    ): EntitiesToSynchronise<CategoryRemoteEntity> {
        return firestoreAdapter.getEntitiesAfterTimestamp(timestamp = timestamp, userId = userId)
    }

}


fun getCategoryRemoteDao(firestore: FirebaseFirestore): CategoryRemoteDao {
    return CategoryRemoteDao(
        firestoreAdapter = FirestoreAdapter(
            firestore = firestore,
            collectionName = TableName.Category.name,
            dataToEntityMapper = Map<String, Any?>::toCategoryRemoteEntity,
            entityToDataMapper = CategoryRemoteEntity::toMap,
            getDocumentIdentifier = { it.id.toString() }
        )
    )
}