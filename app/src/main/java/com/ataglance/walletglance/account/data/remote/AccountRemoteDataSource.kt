package com.ataglance.walletglance.account.data.remote

import com.ataglance.walletglance.account.data.model.AccountEntity
import com.ataglance.walletglance.account.mapper.toAccountEntity
import com.ataglance.walletglance.account.mapper.toMap
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.core.data.remote.BaseRemoteDataSource
import com.google.firebase.firestore.FirebaseFirestore

class AccountRemoteDataSource(
    userId: String,
    firestore: FirebaseFirestore
) : BaseRemoteDataSource<AccountEntity>(
    userId = userId,
    firestore = firestore,
    collectionName = "accounts",
    tableName = TableName.Account,
    getDocumentRef = { this.document(it.id.toString()) },
    dataToEntityMapper = Map<String, Any>::toAccountEntity,
    entityToDataMapper = AccountEntity::toMap
) {

    fun deleteAccountsByIds(
        idList: List<Int>,
        timestamp: Long,
        onSuccessListener: () -> Unit = {},
        onFailureListener: (Exception) -> Unit = {}
    ) {
        val batch = firestore.batch()

        idList.forEach { id ->
            batch.delete(collectionRef.document(id.toString()))
        }

        batch.commit()
            .addOnSuccessListener {
                tableUpdateTimeCollectionRef(timestamp)
                onSuccessListener()
            }
            .addOnFailureListener(onFailureListener)
    }

}
