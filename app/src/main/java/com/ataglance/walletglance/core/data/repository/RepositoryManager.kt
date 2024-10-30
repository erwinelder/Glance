package com.ataglance.walletglance.core.data.repository

import com.ataglance.walletglance.account.data.local.AccountLocalDataSource
import com.ataglance.walletglance.account.data.remote.AccountRemoteDataSource
import com.ataglance.walletglance.account.data.repository.AccountRepository
import com.ataglance.walletglance.account.data.repository.AccountRepositoryImpl
import com.ataglance.walletglance.auth.domain.User
import com.ataglance.walletglance.budget.data.local.BudgetAccountAssociationLocalDataSource
import com.ataglance.walletglance.budget.data.local.BudgetLocalDataSource
import com.ataglance.walletglance.budget.data.remote.BudgetAccountAssociationRemoteDataSource
import com.ataglance.walletglance.budget.data.remote.BudgetRemoteDataSource
import com.ataglance.walletglance.budget.data.repository.BudgetAndBudgetAccountAssociationRepository
import com.ataglance.walletglance.budget.data.repository.BudgetAndBudgetAccountAssociationRepositoryImpl
import com.ataglance.walletglance.category.data.local.CategoryLocalDataSource
import com.ataglance.walletglance.category.data.remote.CategoryRemoteDataSource
import com.ataglance.walletglance.category.data.repository.CategoryRepository
import com.ataglance.walletglance.category.data.repository.CategoryRepositoryImpl
import com.ataglance.walletglance.categoryCollection.data.local.CategoryCollectionCategoryAssociationLocalDataSource
import com.ataglance.walletglance.categoryCollection.data.local.CategoryCollectionLocalDataSource
import com.ataglance.walletglance.categoryCollection.data.remote.CategoryCollectionCategoryAssociationRemoteDataSource
import com.ataglance.walletglance.categoryCollection.data.remote.CategoryCollectionRemoteDataSource
import com.ataglance.walletglance.categoryCollection.data.repository.CategoryCollectionAndCollectionCategoryAssociationRepository
import com.ataglance.walletglance.categoryCollection.data.repository.CategoryCollectionAndCollectionCategoryAssociationRepositoryImpl
import com.ataglance.walletglance.categoryCollection.data.repository.CategoryCollectionRepository
import com.ataglance.walletglance.categoryCollection.data.repository.CategoryCollectionRepositoryImpl
import com.ataglance.walletglance.core.data.local.AppDatabase
import com.ataglance.walletglance.navigation.data.local.NavigationButtonLocalDataSource
import com.ataglance.walletglance.navigation.data.remote.NavigationButtonRemoteDataSource
import com.ataglance.walletglance.navigation.data.repository.NavigationButtonRepository
import com.ataglance.walletglance.navigation.data.repository.NavigationButtonRepositoryImpl
import com.ataglance.walletglance.personalization.data.local.BudgetOnWidgetLocalDataSource
import com.ataglance.walletglance.personalization.data.local.WidgetLocalDataSource
import com.ataglance.walletglance.personalization.data.remote.BudgetOnWidgetRemoteDataSource
import com.ataglance.walletglance.personalization.data.remote.WidgetRemoteDataSource
import com.ataglance.walletglance.personalization.data.repository.BudgetOnWidgetRepository
import com.ataglance.walletglance.personalization.data.repository.BudgetOnWidgetRepositoryImpl
import com.ataglance.walletglance.personalization.data.repository.WidgetRepository
import com.ataglance.walletglance.personalization.data.repository.WidgetRepositoryImpl
import com.ataglance.walletglance.record.data.local.RecordLocalDataSource
import com.ataglance.walletglance.record.data.remote.RecordRemoteDataSource
import com.ataglance.walletglance.record.data.repository.RecordRepository
import com.ataglance.walletglance.record.data.repository.RecordRepositoryImpl
import com.google.firebase.firestore.FirebaseFirestore

class RepositoryManager(
    private val db: AppDatabase,
    private val user: User,
    private val firestore: FirebaseFirestore
) {

    private inline fun <reified RS> createRemoteDataSource(): RS? {
        return if (user.isAliableForDataSync() && user.uid != null) {
            when (RS::class) {
                AccountRemoteDataSource::class -> AccountRemoteDataSource(
                    userId = user.uid, firestore = firestore
                ) as RS
                CategoryRemoteDataSource::class -> CategoryRemoteDataSource(
                    userId = user.uid, firestore = firestore
                ) as RS
                CategoryCollectionRemoteDataSource::class -> CategoryCollectionRemoteDataSource(
                    userId = user.uid, firestore = firestore
                ) as RS
                CategoryCollectionCategoryAssociationRemoteDataSource::class ->
                    CategoryCollectionCategoryAssociationRemoteDataSource(
                        userId = user.uid, firestore = firestore
                    ) as RS
                RecordRemoteDataSource::class -> RecordRemoteDataSource(
                    userId = user.uid, firestore = firestore
                ) as RS
                BudgetRemoteDataSource::class -> BudgetRemoteDataSource(
                    userId = user.uid, firestore = firestore
                ) as RS
                BudgetAccountAssociationRemoteDataSource::class ->
                    BudgetAccountAssociationRemoteDataSource(
                        userId = user.uid, firestore = firestore
                    ) as RS
                NavigationButtonRemoteDataSource::class -> NavigationButtonRemoteDataSource(
                    userId = user.uid, firestore = firestore
                ) as RS
                WidgetRemoteDataSource::class -> WidgetRemoteDataSource(
                    userId = user.uid, firestore = firestore
                ) as RS
                BudgetOnWidgetRemoteDataSource::class -> BudgetOnWidgetRemoteDataSource(
                    userId = user.uid, firestore = firestore
                ) as RS
                else -> null
            }
        } else null
    }

    fun getAccountRepository(): AccountRepository {
        return AccountRepositoryImpl(
            localSource = AccountLocalDataSource(db.accountDao, db.tableUpdateTimeDao),
            remoteSource = createRemoteDataSource<AccountRemoteDataSource>()
        )
    }

    fun getCategoryRepository(): CategoryRepository {
        return CategoryRepositoryImpl(
            localSource = CategoryLocalDataSource(db.categoryDao, db.tableUpdateTimeDao),
            remoteSource = createRemoteDataSource<CategoryRemoteDataSource>()
        )
    }

    fun getCategoryCollectionRepository(): CategoryCollectionRepository {
        return CategoryCollectionRepositoryImpl(
            localSource = CategoryCollectionLocalDataSource(db.categoryCollectionDao, db.tableUpdateTimeDao),
            remoteSource = createRemoteDataSource<CategoryCollectionRemoteDataSource>()
        )
    }

    fun getCategoryCollectionAndCollectionCategoryAssociationRepository():
            CategoryCollectionAndCollectionCategoryAssociationRepository {
        return CategoryCollectionAndCollectionCategoryAssociationRepositoryImpl(
            entityLocalSource = CategoryCollectionLocalDataSource(db.categoryCollectionDao, db.tableUpdateTimeDao),
            entityRemoteSource = createRemoteDataSource<CategoryCollectionRemoteDataSource>(),
            associationLocalSource = CategoryCollectionCategoryAssociationLocalDataSource(
                db.categoryCollectionCategoryAssociationDao, db.tableUpdateTimeDao
            ),
            associationRemoteSource = createRemoteDataSource<CategoryCollectionCategoryAssociationRemoteDataSource>()
        )
    }

    fun getRecordRepository(): RecordRepository {
        return RecordRepositoryImpl(
            localSource = RecordLocalDataSource(db.recordDao, db.tableUpdateTimeDao),
            remoteSource = createRemoteDataSource<RecordRemoteDataSource>()
        )
    }

    fun getBudgetAndBudgetAccountAssociationRepository():
            BudgetAndBudgetAccountAssociationRepository {
        return BudgetAndBudgetAccountAssociationRepositoryImpl(
            entityLocalSource = BudgetLocalDataSource(db.budgetDao, db.tableUpdateTimeDao),
            entityRemoteSource = createRemoteDataSource<BudgetRemoteDataSource>(),
            associationLocalSource = BudgetAccountAssociationLocalDataSource(
                db.budgetAccountAssociationDao, db.tableUpdateTimeDao
            ),
            associationRemoteSource = createRemoteDataSource<BudgetAccountAssociationRemoteDataSource>()
        )
    }

    fun getNavigationButtonRepository(): NavigationButtonRepository {
        return NavigationButtonRepositoryImpl(
            localSource = NavigationButtonLocalDataSource(db.navigationButtonDao, db.tableUpdateTimeDao),
            remoteSource = createRemoteDataSource<NavigationButtonRemoteDataSource>()
        )
    }

    fun getWidgetRepository(): WidgetRepository {
        return WidgetRepositoryImpl(
            localSource = WidgetLocalDataSource(db.widgetDao, db.tableUpdateTimeDao),
            remoteSource = createRemoteDataSource<WidgetRemoteDataSource>()
        )
    }

    fun getBudgetOnWidgetRepository(): BudgetOnWidgetRepository {
        return BudgetOnWidgetRepositoryImpl(
            localSource = BudgetOnWidgetLocalDataSource(db.budgetOnWidgetDao, db.tableUpdateTimeDao),
            remoteSource = createRemoteDataSource<BudgetOnWidgetRemoteDataSource>()
        )
    }

}