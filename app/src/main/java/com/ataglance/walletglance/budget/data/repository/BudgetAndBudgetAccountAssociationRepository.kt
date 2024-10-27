package com.ataglance.walletglance.budget.data.repository

import androidx.room.Transaction
import com.ataglance.walletglance.budget.data.model.BudgetAccountAssociation
import com.ataglance.walletglance.budget.data.model.BudgetEntity

interface BudgetAndBudgetAccountAssociationRepository {

    @Transaction
    suspend fun deleteAndUpsertBudgetsAndDeleteAndUpsertAssociations(
        budgetsIdsToDelete: List<Int>,
        budgetListToUpsert: List<BudgetEntity>,
        associationsToDelete: List<BudgetAccountAssociation>,
        associationsToUpsert: List<BudgetAccountAssociation>
    )

    @Transaction
    suspend fun getBudgetsAndBudgetAccountAssociations():
            Pair<List<BudgetEntity>, List<BudgetAccountAssociation>>

}
