package com.ataglance.walletglance.budget.data.repository

import androidx.room.Transaction
import com.ataglance.walletglance.budget.data.local.BudgetAccountAssociationLocalDataSource
import com.ataglance.walletglance.budget.data.local.BudgetLocalDataSource
import com.ataglance.walletglance.budget.data.model.BudgetAccountAssociation
import com.ataglance.walletglance.budget.data.model.BudgetEntity
import com.ataglance.walletglance.core.utils.getNowDateTimeLong

class BudgetAndBudgetAccountAssociationRepositoryImpl(
    private val budgetLocalSource: BudgetLocalDataSource,
    private val budgetAccountAssociationLocalSource: BudgetAccountAssociationLocalDataSource
) : BudgetAndBudgetAccountAssociationRepository {

    @Transaction
    override suspend fun deleteAndUpsertBudgetsAndDeleteAndUpsertAssociations(
        budgetsIdsToDelete: List<Int>,
        budgetListToUpsert: List<BudgetEntity>,
        associationsToDelete: List<BudgetAccountAssociation>,
        associationsToUpsert: List<BudgetAccountAssociation>
    ) {
        val timestamp = getNowDateTimeLong()

        budgetLocalSource.deleteAndUpsertBudgets(
            idsToDelete = budgetsIdsToDelete,
            budgetsToUpsert = budgetListToUpsert,
            timestamp = timestamp
        )
        budgetAccountAssociationLocalSource.deleteAndUpsertAssociations(
            associationsToDelete = associationsToDelete,
            associationsToUpsert = associationsToUpsert,
            timestamp = timestamp
        )
    }

    @Transaction
    override suspend fun getBudgetsAndBudgetAccountAssociations():
            Pair<List<BudgetEntity>, List<BudgetAccountAssociation>>
    {

    }

}
