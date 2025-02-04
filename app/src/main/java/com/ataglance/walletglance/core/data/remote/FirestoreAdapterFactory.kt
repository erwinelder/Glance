package com.ataglance.walletglance.core.data.remote

import com.ataglance.walletglance.account.data.mapper.toAccountRemoteEntity
import com.ataglance.walletglance.account.data.mapper.toMap
import com.ataglance.walletglance.account.data.remote.model.AccountRemoteEntity
import com.ataglance.walletglance.category.data.mapper.toCategoryRemoteEntity
import com.ataglance.walletglance.category.data.mapper.toMap
import com.ataglance.walletglance.category.data.remote.model.CategoryRemoteEntity
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.record.data.mapper.toMap
import com.ataglance.walletglance.record.data.mapper.toRecordRemoteEntity
import com.ataglance.walletglance.record.data.remote.model.RecordRemoteEntity
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreAdapterFactory(
    private val firestore: FirebaseFirestore
) {

    fun getAccountFirestoreAdapter(): FirestoreAdapter<AccountRemoteEntity> {
        return FirestoreAdapterImpl(
            firestore = firestore,
            collectionName = TableName.Account.name,
            dataToEntityMapper = Map<String, Any?>::toAccountRemoteEntity,
            entityToDataMapper = AccountRemoteEntity::toMap,
            getDocumentIdentifier = { it.id.toString() }
        )
    }

    fun getCategoryFirestoreAdapter(): FirestoreAdapter<CategoryRemoteEntity> {
        return FirestoreAdapterImpl(
            firestore = firestore,
            collectionName = TableName.Category.name,
            dataToEntityMapper = Map<String, Any?>::toCategoryRemoteEntity,
            entityToDataMapper = CategoryRemoteEntity::toMap,
            getDocumentIdentifier = { it.id.toString() }
        )
    }

    fun getRecordFirestoreAdapter(): FirestoreAdapter<RecordRemoteEntity> {
        return FirestoreAdapterImpl(
            firestore = firestore,
            collectionName = TableName.Record.name,
            dataToEntityMapper = Map<String, Any?>::toRecordRemoteEntity,
            entityToDataMapper = RecordRemoteEntity::toMap,
            getDocumentIdentifier = { it.id.toString() }
        )
    }

}