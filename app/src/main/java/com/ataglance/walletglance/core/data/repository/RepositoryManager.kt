package com.ataglance.walletglance.core.data.repository

import com.ataglance.walletglance.account.data.local.AccountLocalDataSource
import com.ataglance.walletglance.account.data.remote.AccountRemoteDataSource
import com.ataglance.walletglance.account.data.repository.AccountRepository
import com.ataglance.walletglance.account.data.repository.AccountRepositoryImpl
import com.ataglance.walletglance.auth.domain.User
import com.ataglance.walletglance.budget.data.repository.BudgetAndBudgetAccountAssociationRepository
import com.ataglance.walletglance.category.data.repository.CategoryRepository
import com.ataglance.walletglance.categoryCollection.data.repository.CategoryCollectionAndCollectionCategoryAssociationRepository
import com.ataglance.walletglance.categoryCollection.data.repository.CategoryCollectionRepository
import com.ataglance.walletglance.core.data.local.AppDatabase
import com.ataglance.walletglance.record.data.repository.RecordRepository
import com.ataglance.walletglance.recordAndAccount.data.repository.RecordAndAccountRepository
import com.google.firebase.firestore.FirebaseFirestore

class RepositoryManager(
    private val db: AppDatabase,
    private val user: User,
    private val firestore: FirebaseFirestore
) {

    fun getAccountRepository(): AccountRepository {
        val localSource = AccountLocalDataSource(db.accountDao, db.tableUpdateTimeDao)
        val remoteSource = if (user.isAliableForDataSync() && user.uid != null) {
            AccountRemoteDataSource(userId = user.uid, firestore = firestore)
        } else null

        return AccountRepositoryImpl(localSource = localSource, remoteSource = remoteSource)
    }

    fun getCategoryRepository(): CategoryRepository {
        return CategoryRepository(db.categoryDao)
    }

    fun getCategoryCollectionRepository(): CategoryCollectionRepository {
        return CategoryCollectionRepository(db.categoryCollectionDao)
    }

    fun getCategoryCollectionAndCollectionCategoryAssociationRepository():
            CategoryCollectionAndCollectionCategoryAssociationRepository {

        return CategoryCollectionAndCollectionCategoryAssociationRepository(
            categoryCollectionDao = db.categoryCollectionDao,
            categoryCollectionCategoryAssociationDao = db.categoryCollectionCategoryAssociationDao
        )
    }

    fun getRecordRepository(): RecordRepository {
        return RecordRepository(db.recordDao)
    }

    fun getBudgetAndBudgetAccountAssociationRepository():
            BudgetAndBudgetAccountAssociationRepository {

        return BudgetAndBudgetAccountAssociationRepository(
            budgetDao = db.budgetDao,
            budgetAccountAssociationDao = db.budgetAccountAssociationDao
        )
    }

    fun getRecordAndAccountRepository(): RecordAndAccountRepository {
        return RecordAndAccountRepository(db.recordDao, db.accountDao)
    }

}