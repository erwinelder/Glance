package com.ataglance.walletglance.categoryCollection.data.remote

import com.ataglance.walletglance.categoryCollection.data.model.CategoryCollectionCategoryAssociation
import com.ataglance.walletglance.categoryCollection.mapper.toCategoryCollectionCategoryAssociation
import com.ataglance.walletglance.categoryCollection.mapper.toMap
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.core.data.remote.BaseRemoteDataSource
import com.google.firebase.firestore.FirebaseFirestore

class CategoryCollectionCategoryAssociationRemoteDataSource(
    userId: String,
    firestore: FirebaseFirestore
) : BaseRemoteDataSource<CategoryCollectionCategoryAssociation>(
    userId = userId,
    firestore = firestore,
    collectionName = "categoryCollectionCategoryAssociations",
    tableName = TableName.CategoryCollectionCategoryAssociation,
    getDocumentRef = { this.document("${it.categoryCollectionId}-${it.categoryId}") },
    dataToEntityMapper = Map<String, Any?>::toCategoryCollectionCategoryAssociation,
    entityToDataMapper = CategoryCollectionCategoryAssociation::toMap
)