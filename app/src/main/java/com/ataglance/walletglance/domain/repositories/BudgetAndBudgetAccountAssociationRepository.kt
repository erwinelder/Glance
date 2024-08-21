package com.ataglance.walletglance.domain.repositories

import androidx.room.Transaction
import com.ataglance.walletglance.domain.dao.BudgetAccountAssociationDao
import com.ataglance.walletglance.domain.dao.BudgetDao
import com.ataglance.walletglance.domain.entities.BudgetAccountAssociation
import com.ataglance.walletglance.domain.entities.BudgetEntity

class BudgetAndBudgetAccountAssociationRepository(
    private val budgetDao: BudgetDao,
    private val budgetAccountAssociationDao: BudgetAccountAssociationDao
) {

    @Transaction
    suspend fun getBudgetsAndBudgetAccountAssociations():
            Pair<List<BudgetEntity>, List<BudgetAccountAssociation>>
    {
        val budgets = budgetDao.getAllBudgets()
        val associations = budgetAccountAssociationDao.getAllAssociations()
        return budgets to associations
    }

    @Transaction
    suspend fun deleteAndUpsertBudgetsAndDeleteAndUpsertAssociations(
        budgetsIdsToDelete: List<Int>,
        budgetListToUpsert: List<BudgetEntity>,
        associationsToDelete: List<BudgetAccountAssociation>,
        associationsToUpsert: List<BudgetAccountAssociation>
    ) {
        if (budgetsIdsToDelete.isNotEmpty())
            budgetDao.deleteBudgetsByIds(budgetsIdsToDelete)
        if (budgetListToUpsert.isNotEmpty())
            budgetDao.upsertBudgets(budgetListToUpsert)

        if (associationsToDelete.isNotEmpty())
            budgetAccountAssociationDao.deleteAssociations(associationsToDelete)
        if (associationsToUpsert.isNotEmpty())
            budgetAccountAssociationDao.upsertAssociations(associationsToUpsert)
    }

}