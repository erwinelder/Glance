package com.ataglance.walletglance.budget.data.repository

import androidx.room.Transaction
import com.ataglance.walletglance.budget.data.local.dao.BudgetAccountAssociationDao
import com.ataglance.walletglance.budget.data.local.dao.BudgetDao
import com.ataglance.walletglance.budget.data.local.model.BudgetAccountAssociation
import com.ataglance.walletglance.budget.data.local.model.BudgetEntity

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