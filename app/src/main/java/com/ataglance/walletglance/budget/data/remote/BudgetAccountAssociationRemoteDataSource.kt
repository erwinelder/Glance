package com.ataglance.walletglance.budget.data.remote

import com.ataglance.walletglance.budget.data.model.BudgetAccountAssociation
import com.ataglance.walletglance.budget.mapper.toBudgetAccountAssociation
import com.ataglance.walletglance.budget.mapper.toMap
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.core.data.remote.BaseRemoteDataSource
import com.google.firebase.firestore.FirebaseFirestore

class BudgetAccountAssociationRemoteDataSource(
    userId: String,
    firestore: FirebaseFirestore
) : BaseRemoteDataSource<BudgetAccountAssociation>(
    userId = userId,
    firestore = firestore,
    collectionName = "budgetAccountAssociations",
    tableName = TableName.BudgetAccountAssociation,
    getDocumentRef = { this.document("${it.budgetId}-${it.accountId}") },
    dataToEntityMapper = Map<String, Any?>::toBudgetAccountAssociation,
    entityToDataMapper = BudgetAccountAssociation::toMap
)