package com.ataglance.walletglance.categoryCollection.data.remote

import com.ataglance.walletglance.categoryCollection.data.model.CategoryCollectionEntity
import com.ataglance.walletglance.categoryCollection.mapper.toCategoryCollectionEntity
import com.ataglance.walletglance.categoryCollection.mapper.toMap
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.core.data.remote.BaseRemoteDataSource
import com.google.firebase.firestore.FirebaseFirestore

class CategoryCollectionRemoteDataSource(
    userId: String,
    firestore: FirebaseFirestore
) : BaseRemoteDataSource<CategoryCollectionEntity>(
    userId = userId,
    firestore = firestore,
    collectionName = "categoryCollections",
    tableName = TableName.CategoryCollection,
    getDocumentRef = { this.document(it.id.toString()) },
    dataToEntityMapper = Map<String, Any?>::toCategoryCollectionEntity,
    entityToDataMapper = CategoryCollectionEntity::toMap
)