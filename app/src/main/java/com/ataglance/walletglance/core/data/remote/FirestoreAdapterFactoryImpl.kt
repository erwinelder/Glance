package com.ataglance.walletglance.core.data.remote

import com.ataglance.walletglance.account.data.mapper.toAccountRemoteEntity
import com.ataglance.walletglance.account.data.mapper.toMap
import com.ataglance.walletglance.account.data.remote.model.AccountRemoteEntity
import com.ataglance.walletglance.budget.data.mapper.budget.toBudgetAccountRemoteAssociation
import com.ataglance.walletglance.budget.data.mapper.budget.toBudgetRemoteEntity
import com.ataglance.walletglance.budget.data.mapper.budget.toMap
import com.ataglance.walletglance.budget.data.mapper.budgetOnWidget.toBudgetOnWidgetRemoteEntity
import com.ataglance.walletglance.budget.data.mapper.budgetOnWidget.toMap
import com.ataglance.walletglance.budget.data.remote.model.BudgetAccountRemoteAssociation
import com.ataglance.walletglance.budget.data.remote.model.BudgetOnWidgetRemoteEntity
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
import com.ataglance.walletglance.navigation.data.mapper.toMap
import com.ataglance.walletglance.navigation.data.mapper.toNavigationButtonRemoteEntity
import com.ataglance.walletglance.navigation.data.remote.model.NavigationButtonRemoteEntity
import com.ataglance.walletglance.personalization.data.mapper.toMap
import com.ataglance.walletglance.personalization.data.mapper.toWidgetRemoteEntity
import com.ataglance.walletglance.personalization.data.remote.model.WidgetRemoteEntity
import com.ataglance.walletglance.record.data.mapper.toMap
import com.ataglance.walletglance.record.data.mapper.toRecordRemoteEntity
import com.ataglance.walletglance.record.data.remote.model.RecordRemoteEntity
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreAdapterFactoryImpl(
    private val firestore: FirebaseFirestore
) : FirestoreAdapterFactory {

    override fun getAccountFirestoreAdapter(): FirestoreAdapter<AccountRemoteEntity> {
        return FirestoreAdapterImpl(
            firestore = firestore,
            collectionName = TableName.Account.name,
            dataToEntityMapper = Map<String, Any?>::toAccountRemoteEntity,
            entityToDataMapper = AccountRemoteEntity::toMap,
            getDocumentIdentifier = { it.id.toString() }
        )
    }

    override fun getCategoryFirestoreAdapter(): FirestoreAdapter<CategoryRemoteEntity> {
        return FirestoreAdapterImpl(
            firestore = firestore,
            collectionName = TableName.Category.name,
            dataToEntityMapper = Map<String, Any?>::toCategoryRemoteEntity,
            entityToDataMapper = CategoryRemoteEntity::toMap,
            getDocumentIdentifier = { it.id.toString() }
        )
    }

    override fun getRecordFirestoreAdapter(): FirestoreAdapter<RecordRemoteEntity> {
        return FirestoreAdapterImpl(
            firestore = firestore,
            collectionName = TableName.Record.name,
            dataToEntityMapper = Map<String, Any?>::toRecordRemoteEntity,
            entityToDataMapper = RecordRemoteEntity::toMap,
            getDocumentIdentifier = { it.id.toString() }
        )
    }

    override fun getCategoryCollectionFirestoreAdapter():
            FirestoreAdapter<CategoryCollectionRemoteEntity>
    {
        return FirestoreAdapterImpl(
            firestore = firestore,
            collectionName = TableName.CategoryCollection.name,
            dataToEntityMapper = Map<String, Any?>::toCategoryCollectionRemoteEntity,
            entityToDataMapper = CategoryCollectionRemoteEntity::toMap,
            getDocumentIdentifier = { it.id.toString() }
        )
    }

    override fun getCollectionCategoryAssociationFirestoreAdapter():
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

    override fun getBudgetFirestoreAdapter(): FirestoreAdapter<BudgetRemoteEntity> {
        return FirestoreAdapterImpl(
            firestore = firestore,
            collectionName = TableName.Budget.name,
            dataToEntityMapper = Map<String, Any?>::toBudgetRemoteEntity,
            entityToDataMapper = BudgetRemoteEntity::toMap,
            getDocumentIdentifier = { it.id.toString() }
        )
    }

    override fun getBudgetAccountAssociationFirestoreAdapter():
            FirestoreAdapter<BudgetAccountRemoteAssociation>
    {
        return FirestoreAdapterImpl(
            firestore = firestore,
            collectionName = TableName.BudgetAccountAssociation.name,
            dataToEntityMapper = Map<String, Any?>::toBudgetAccountRemoteAssociation,
            entityToDataMapper = BudgetAccountRemoteAssociation::toMap,
            getDocumentIdentifier = { "${it.budgetId}-${it.accountId}" }
        )
    }

    override fun getNavigationButtonFirestoreAdapter():
            FirestoreAdapter<NavigationButtonRemoteEntity>
    {
        return FirestoreAdapterImpl(
            firestore = firestore,
            collectionName = TableName.NavigationButton.name,
            dataToEntityMapper = Map<String, Any?>::toNavigationButtonRemoteEntity,
            entityToDataMapper = NavigationButtonRemoteEntity::toMap,
            getDocumentIdentifier = { it.screenName }
        )
    }

    override fun getWidgetFirestoreAdapter(): FirestoreAdapter<WidgetRemoteEntity> {
        return FirestoreAdapterImpl(
            firestore = firestore,
            collectionName = TableName.Widget.name,
            dataToEntityMapper = Map<String, Any?>::toWidgetRemoteEntity,
            entityToDataMapper = WidgetRemoteEntity::toMap,
            getDocumentIdentifier = { it.name }
        )
    }

    override fun getBudgetOnWidgetFirestoreAdapter(): FirestoreAdapter<BudgetOnWidgetRemoteEntity> {
        return FirestoreAdapterImpl(
            firestore = firestore,
            collectionName = TableName.BudgetOnWidget.name,
            dataToEntityMapper = Map<String, Any?>::toBudgetOnWidgetRemoteEntity,
            entityToDataMapper = BudgetOnWidgetRemoteEntity::toMap,
            getDocumentIdentifier = { it.budgetId.toString() }
        )
    }

}