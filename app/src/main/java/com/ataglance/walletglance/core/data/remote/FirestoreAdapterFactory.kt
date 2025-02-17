package com.ataglance.walletglance.core.data.remote

import com.ataglance.walletglance.account.data.remote.model.AccountRemoteEntity
import com.ataglance.walletglance.budget.data.remote.model.BudgetAccountRemoteAssociation
import com.ataglance.walletglance.budget.data.remote.model.BudgetOnWidgetRemoteEntity
import com.ataglance.walletglance.budget.data.remote.model.BudgetRemoteEntity
import com.ataglance.walletglance.category.data.remote.model.CategoryRemoteEntity
import com.ataglance.walletglance.categoryCollection.data.remote.model.CategoryCollectionCategoryRemoteAssociation
import com.ataglance.walletglance.categoryCollection.data.remote.model.CategoryCollectionRemoteEntity
import com.ataglance.walletglance.navigation.data.remote.model.NavigationButtonRemoteEntity
import com.ataglance.walletglance.personalization.data.remote.model.WidgetRemoteEntity
import com.ataglance.walletglance.record.data.remote.model.RecordRemoteEntity

interface FirestoreAdapterFactory {

    fun getAccountFirestoreAdapter(): FirestoreAdapter<AccountRemoteEntity>

    fun getCategoryFirestoreAdapter(): FirestoreAdapter<CategoryRemoteEntity>

    fun getRecordFirestoreAdapter(): FirestoreAdapter<RecordRemoteEntity>

    fun getCategoryCollectionFirestoreAdapter(): FirestoreAdapter<CategoryCollectionRemoteEntity>

    fun getCollectionCategoryAssociationFirestoreAdapter():
            FirestoreAdapter<CategoryCollectionCategoryRemoteAssociation>


    fun getBudgetFirestoreAdapter(): FirestoreAdapter<BudgetRemoteEntity>

    fun getBudgetAccountAssociationFirestoreAdapter(): FirestoreAdapter<BudgetAccountRemoteAssociation>

    fun getNavigationButtonFirestoreAdapter(): FirestoreAdapter<NavigationButtonRemoteEntity>

    fun getWidgetFirestoreAdapter(): FirestoreAdapter<WidgetRemoteEntity>

    fun getBudgetOnWidgetFirestoreAdapter(): FirestoreAdapter<BudgetOnWidgetRemoteEntity>

}