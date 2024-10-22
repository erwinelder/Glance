package com.ataglance.walletglance.account.data.remote

import com.ataglance.walletglance.account.data.mapper.toAccountEntity
import com.ataglance.walletglance.account.data.mapper.toMap
import com.ataglance.walletglance.account.data.model.AccountEntity
import com.ataglance.walletglance.core.data.remote.BaseRemoteDataSource
import com.google.firebase.firestore.FirebaseFirestore

class AccountRemoteDataSource(
    userId: String,
    firestore: FirebaseFirestore
) : BaseRemoteDataSource<AccountEntity>(
    userId = userId,
    firestore = firestore,
    collectionName = "accounts",
    getDocumentRef = { this.document(it.id.toString()) },
    dataToEntityMapper = Map<String, Any>::toAccountEntity,
    entityToDataMapper = AccountEntity::toMap
) {

    fun deleteAccountsByIds(
        idList: List<Int>,
        onSuccessListener: () -> Unit = {},
        onFailureListener: (Exception) -> Unit = {}
    ) {
        idList.forEach { id ->
            collectionRef.document(id.toString())
                .delete()
                .addOnSuccessListener { onSuccessListener() }
                .addOnFailureListener(onFailureListener)
        }
    }

}
