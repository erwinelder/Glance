package com.ataglance.walletglance.core.data.remote

import com.ataglance.walletglance.account.data.mapper.toAccountRemoteEntity
import com.ataglance.walletglance.account.data.mapper.toMap
import com.ataglance.walletglance.account.data.remote.model.AccountRemoteEntity
import com.ataglance.walletglance.budget.data.mapper.toBudgetAccountRemoteAssociation
import com.ataglance.walletglance.budget.data.mapper.toBudgetRemoteEntity
import com.ataglance.walletglance.budget.data.mapper.toMap
import com.ataglance.walletglance.budget.data.remote.model.BudgetAccountRemoteAssociation
import com.ataglance.walletglance.budget.data.remote.model.BudgetRemoteEntity
import com.ataglance.walletglance.category.data.mapper.toCategoryRemoteEntity
import com.ataglance.walletglance.category.data.mapper.toMap
import com.ataglance.walletglance.category.data.remote.model.CategoryRemoteEntity
import com.ataglance.walletglance.categoryCollection.data.mapper.toCategoryCollectionCategoryRemoteAssociation
import com.ataglance.walletglance.categoryCollection.data.mapper.toCategoryCollectionRemoteEntity
import com.ataglance.walletglance.categoryCollection.data.mapper.toMap
import com.ataglance.walletglance.categoryCollection.data.remote.model.CategoryCollectionCategoryRemoteAssociation
import com.ataglance.walletglance.categoryCollection.data.remote.model.CategoryCollectionRemoteEntity
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

    fun getCategoryCollectionFirestoreAdapter(): FirestoreAdapter<CategoryCollectionRemoteEntity> {
        return FirestoreAdapterImpl(
            firestore = firestore,
            collectionName = TableName.CategoryCollection.name,
            dataToEntityMapper = Map<String, Any?>::toCategoryCollectionRemoteEntity,
            entityToDataMapper = CategoryCollectionRemoteEntity::toMap,
            getDocumentIdentifier = { it.id.toString() }
        )
    }

    fun getCollectionCategoryAssociationFirestoreAdapter():
            FirestoreAdapter<CategoryCollectionCategoryRemoteAssociation>
    {
        return FirestoreAdapterImpl(
            firestore = firestore,
            collectionName = TableName.CategoryCollectionCategoryAssociation.name,
            dataToEntityMapper = Map<String, Any?>::toCategoryCollectionCategoryRemoteAssociation,
            entityToDataMapper = CategoryCollectionCategoryRemoteAssociation::toMap,
            getDocumentIdentifier = { "${it.categoryCollectionId}-${it.categoryId}" }
        )
    }

    fun getBudgetFirestoreAdapter(): FirestoreAdapter<BudgetRemoteEntity> {
        return FirestoreAdapterImpl(
            firestore = firestore,
            collectionName = TableName.Budget.name,
            dataToEntityMapper = Map<String, Any?>::toBudgetRemoteEntity,
            entityToDataMapper = BudgetRemoteEntity::toMap,
            getDocumentIdentifier = { it.id.toString() }
        )
    }

    fun getBudgetAccountAssociationFirestoreAdapter(): FirestoreAdapter<BudgetAccountRemoteAssociation> {
        return FirestoreAdapterImpl(
            firestore = firestore,
            collectionName = TableName.BudgetAccountAssociation.name,
            dataToEntityMapper = Map<String, Any?>::toBudgetAccountRemoteAssociation,
            entityToDataMapper = BudgetAccountRemoteAssociation::toMap,
            getDocumentIdentifier = { "${it.budgetId}-${it.accountId}" }
        )
    }

}