package com.ataglance.walletglance.budget.data.repository

import androidx.room.Transaction
import com.ataglance.walletglance.budget.data.local.BudgetAccountAssociationLocalDataSource
import com.ataglance.walletglance.budget.data.local.BudgetLocalDataSource
import com.ataglance.walletglance.budget.data.model.BudgetAccountAssociation
import com.ataglance.walletglance.budget.data.model.BudgetEntity
import com.ataglance.walletglance.budget.data.remote.BudgetAccountAssociationRemoteDataSource
import com.ataglance.walletglance.budget.data.remote.BudgetRemoteDataSource
import com.ataglance.walletglance.core.utils.getNowDateTimeLong
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach

class BudgetAndBudgetAccountAssociationRepositoryImpl(
    private val budgetLocalSource: BudgetLocalDataSource,
    private val budgetRemoteDataSource: BudgetRemoteDataSource?,
    private val associationLocalSource: BudgetAccountAssociationLocalDataSource,
    private val associationRemoteDataSource: BudgetAccountAssociationRemoteDataSource?
) : BudgetAndBudgetAccountAssociationRepository {

    @Transaction
    override suspend fun deleteAndUpsertBudgetsAndDeleteAndUpsertAssociations(
        budgetListToDelete: List<BudgetEntity>,
        budgetListToUpsert: List<BudgetEntity>,
        associationsToDelete: List<BudgetAccountAssociation>,
        associationsToUpsert: List<BudgetAccountAssociation>,
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ) {
        val timestamp = getNowDateTimeLong()

        budgetLocalSource.deleteAndUpsertEntities(
            entitiesToDelete = budgetListToDelete,
            entitiesToUpsert = budgetListToUpsert,
            timestamp = timestamp
        )
        associationLocalSource.deleteAndUpsertEntities(
            entitiesToDelete = associationsToDelete,
            entitiesToUpsert = associationsToUpsert,
            timestamp = timestamp
        )

        budgetRemoteDataSource?.deleteAndUpsertEntities(
            entitiesToDelete = budgetListToDelete,
            entitiesToUpsert = budgetListToUpsert,
            timestamp = timestamp,
            onSuccessListener = onSuccessListener,
            onFailureListener = onFailureListener
        )
        associationRemoteDataSource?.deleteAndUpsertEntities(
            entitiesToDelete = associationsToDelete,
            entitiesToUpsert = associationsToUpsert,
            timestamp = timestamp,
            onSuccessListener = onSuccessListener,
            onFailureListener = onFailureListener
        )
    }

    override fun getBudgetsAndBudgetAccountAssociations(
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ): Flow<Pair<List<BudgetEntity>, List<BudgetAccountAssociation>>> = flow {
        try {

            val budgetsLocalTimestamp = budgetLocalSource.getLastModifiedTime()
            val budgetsRemoteTimestamp = budgetRemoteDataSource?.getLastModifiedTime()
                ?: budgetsLocalTimestamp
            val associationsLocalTimestamp = associationLocalSource.getLastModifiedTime()
            val associationsRemoteTimestamp = associationRemoteDataSource?.getLastModifiedTime()
                ?: associationsLocalTimestamp

            val budgetsFlow = selectBudgetsFlow(
                localTimestamp = budgetsLocalTimestamp,
                remoteTimestamp = budgetsRemoteTimestamp
            )
            val associationsFlow = selectAssociationsFlow(
                localTimestamp = associationsLocalTimestamp,
                remoteTimestamp = associationsRemoteTimestamp
            )

            emitAll(combine(budgetsFlow, associationsFlow) { budgets, associations ->
                Pair(budgets, associations)
            })

        } catch (e: Exception) {
            onFailureListener(e)
        }
    }.flowOn(Dispatchers.IO)

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun selectBudgetsFlow(
        localTimestamp: Long, remoteTimestamp: Long
    ): Flow<List<BudgetEntity>> {
        return if (budgetRemoteDataSource != null && remoteTimestamp > localTimestamp) {
            budgetRemoteDataSource.getEntitiesAfterTimestamp(localTimestamp)
                .onEach { entitiesToDeleteAndUpsert ->
                    budgetLocalSource.deleteAndUpsertEntities(
                        entitiesToDeleteAndUpsert = entitiesToDeleteAndUpsert,
                        timestamp = remoteTimestamp
                    )
                }
                .flatMapConcat { budgetLocalSource.getAllBudgets() }
        } else {
            budgetLocalSource.getAllBudgets()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun selectAssociationsFlow(
        localTimestamp: Long, remoteTimestamp: Long
    ): Flow<List<BudgetAccountAssociation>> {
        return if (associationRemoteDataSource != null && remoteTimestamp > localTimestamp) {
            associationRemoteDataSource.getEntitiesAfterTimestamp(localTimestamp)
                .onEach { entitiesToDeleteAndUpsert ->
                    associationLocalSource.deleteAndUpsertEntities(
                        entitiesToDeleteAndUpsert = entitiesToDeleteAndUpsert,
                        timestamp = remoteTimestamp
                    )
                }
                .flatMapConcat { associationLocalSource.getAllAssociations() }
        } else {
            associationLocalSource.getAllAssociations()
        }
    }

}
