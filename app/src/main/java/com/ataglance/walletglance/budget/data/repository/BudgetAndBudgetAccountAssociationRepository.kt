package com.ataglance.walletglance.budget.data.repository

import androidx.room.Transaction
import com.ataglance.walletglance.budget.data.model.BudgetAccountAssociation
import com.ataglance.walletglance.budget.data.model.BudgetEntity
import kotlinx.coroutines.flow.Flow

interface BudgetAndBudgetAccountAssociationRepository {

    @Transaction
    suspend fun deleteAndUpsertBudgetsAndDeleteAndUpsertAssociations(
        budgetListToDelete: List<BudgetEntity>,
        budgetListToUpsert: List<BudgetEntity>,
        associationsToDelete: List<BudgetAccountAssociation>,
        associationsToUpsert: List<BudgetAccountAssociation>,
        onSuccessListener: () -> Unit = {},
        onFailureListener: (Exception) -> Unit = {}
    )

    fun getBudgetsAndBudgetAccountAssociations(
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ): Flow<Pair<List<BudgetEntity>, List<BudgetAccountAssociation>>>

}
