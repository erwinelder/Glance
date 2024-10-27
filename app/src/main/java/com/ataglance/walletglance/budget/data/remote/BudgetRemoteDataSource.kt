package com.ataglance.walletglance.budget.data.remote

import com.ataglance.walletglance.budget.data.model.BudgetEntity
import com.ataglance.walletglance.budget.mapper.toBudgetEntity
import com.ataglance.walletglance.budget.mapper.toMap
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.core.data.remote.BaseRemoteDataSource
import com.google.firebase.firestore.FirebaseFirestore

class BudgetRemoteDataSource(
    userId: String,
    firestore: FirebaseFirestore
) : BaseRemoteDataSource<BudgetEntity>(
    userId = userId,
    firestore = firestore,
    collectionName = "budgets",
    tableName = TableName.Budget,
    getDocumentRef = { this.document(it.id.toString()) },
    dataToEntityMapper = Map<String, Any?>::toBudgetEntity,
    entityToDataMapper = BudgetEntity::toMap
)