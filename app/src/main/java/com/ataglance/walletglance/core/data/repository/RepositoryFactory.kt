package com.ataglance.walletglance.core.data.repository

import com.ataglance.walletglance.auth.domain.model.User
import com.ataglance.walletglance.core.data.local.database.AppDatabase
import com.ataglance.walletglance.personalization.data.local.BudgetOnWidgetLocalDataSource
import com.ataglance.walletglance.personalization.data.local.WidgetLocalDataSource
import com.ataglance.walletglance.personalization.data.remote.BudgetOnWidgetRemoteDataSource
import com.ataglance.walletglance.personalization.data.remote.WidgetRemoteDataSource
import com.ataglance.walletglance.personalization.data.repository.BudgetOnWidgetRepository
import com.ataglance.walletglance.personalization.data.repository.BudgetOnWidgetRepositoryImpl
import com.ataglance.walletglance.personalization.data.repository.WidgetRepository
import com.ataglance.walletglance.personalization.data.repository.WidgetRepositoryImpl
import com.google.firebase.firestore.FirebaseFirestore

class RepositoryFactory(
    private val db: AppDatabase,
    private val user: User,
    private val firestore: FirebaseFirestore
) {

    private inline fun <reified RS> createRemoteDataSource(): RS? {
        if (!user.isEligibleForDataSync() || user.uid == null) return null

        return when (RS::class) {
            WidgetRemoteDataSource::class -> WidgetRemoteDataSource(
                userId = user.uid, firestore = firestore
            ) as RS
            BudgetOnWidgetRemoteDataSource::class -> BudgetOnWidgetRemoteDataSource(
                userId = user.uid, firestore = firestore
            ) as RS
            else -> null
        }
    }

    fun getWidgetRepository(): WidgetRepository {
        return WidgetRepositoryImpl(
            localSource = WidgetLocalDataSource(db.widgetDao, db.localUpdateTimeDao),
            remoteSource = createRemoteDataSource<WidgetRemoteDataSource>()
        )
    }

    fun getBudgetOnWidgetRepository(): BudgetOnWidgetRepository {
        return BudgetOnWidgetRepositoryImpl(
            localSource = BudgetOnWidgetLocalDataSource(db.budgetOnWidgetDao, db.localUpdateTimeDao),
            remoteSource = createRemoteDataSource<BudgetOnWidgetRemoteDataSource>()
        )
    }

}