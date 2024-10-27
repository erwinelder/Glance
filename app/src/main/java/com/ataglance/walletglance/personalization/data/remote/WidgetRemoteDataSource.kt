package com.ataglance.walletglance.personalization.data.remote

import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.core.data.remote.BaseRemoteDataSource
import com.ataglance.walletglance.personalization.data.model.WidgetEntity
import com.ataglance.walletglance.personalization.mapper.toMap
import com.ataglance.walletglance.personalization.mapper.toWidgetEntity
import com.google.firebase.firestore.FirebaseFirestore

class WidgetRemoteDataSource(
    userId: String,
    firestore: FirebaseFirestore
) : BaseRemoteDataSource<WidgetEntity>(
    userId = userId,
    firestore = firestore,
    collectionName = "widgets",
    tableName = TableName.Widget,
    getDocumentRef = { this.document(it.name) },
    dataToEntityMapper = Map<String, Any?>::toWidgetEntity,
    entityToDataMapper = WidgetEntity::toMap
)