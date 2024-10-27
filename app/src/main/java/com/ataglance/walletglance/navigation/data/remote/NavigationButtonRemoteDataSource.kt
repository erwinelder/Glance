package com.ataglance.walletglance.navigation.data.remote

import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.core.data.remote.BaseRemoteDataSource
import com.ataglance.walletglance.navigation.data.model.NavigationButtonEntity
import com.ataglance.walletglance.navigation.mapper.toMap
import com.ataglance.walletglance.navigation.mapper.toNavigationButtonEntity
import com.google.firebase.firestore.FirebaseFirestore

class NavigationButtonRemoteDataSource(
    userId: String,
    firestore: FirebaseFirestore
) : BaseRemoteDataSource<NavigationButtonEntity>(
    userId = userId,
    firestore = firestore,
    collectionName = "navigationButtons",
    tableName = TableName.NavigationButton,
    getDocumentRef = { this.document(it.screenName) },
    dataToEntityMapper = Map<String, Any?>::toNavigationButtonEntity,
    entityToDataMapper = NavigationButtonEntity::toMap
)