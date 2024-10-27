package com.ataglance.walletglance.category.data.remote

import com.ataglance.walletglance.category.data.model.CategoryEntity
import com.ataglance.walletglance.category.mapper.toCategoryEntity
import com.ataglance.walletglance.category.mapper.toMap
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.core.data.remote.BaseRemoteDataSource
import com.google.firebase.firestore.FirebaseFirestore

class CategoryRemoteDataSource(
    userId: String,
    firestore: FirebaseFirestore
) : BaseRemoteDataSource<CategoryEntity>(
    userId = userId,
    firestore = firestore,
    collectionName = "categories",
    tableName = TableName.Category,
    getDocumentRef = { this.document(it.id.toString()) },
    dataToEntityMapper = Map<String, Any?>::toCategoryEntity,
    entityToDataMapper = CategoryEntity::toMap
)