package com.ataglance.walletglance.budget.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.ataglance.walletglance.budget.data.local.model.BudgetAccountAssociationEntity
import com.ataglance.walletglance.budget.data.local.model.BudgetEntity

@Dao
interface BudgetLocalDao {

    @Upsert
    suspend fun upsertBudgets(budgets: List<BudgetEntity>)

    @Delete
    suspend fun deleteBudgets(budgets: List<BudgetEntity>)

    @Query("SELECT * FROM budget WHERE timestamp > :timestamp")
    suspend fun getBudgetsAfterTimestamp(timestamp: Long): List<BudgetEntity>

    @Query("SELECT * FROM budget WHERE id = :id AND deleted = 0")
    suspend fun getBudget(id: Int): BudgetEntity?

    @Query("SELECT * FROM budget WHERE deleted = 0")
    suspend fun getAllBudgets(): List<BudgetEntity>


    @Upsert
    suspend fun upsertBudgetAccountAssociations(associations: List<BudgetAccountAssociationEntity>)

    @Delete
    suspend fun deleteBudgetAccountAssociations(associations: List<BudgetAccountAssociationEntity>)

    @Query("SELECT * FROM budget_account_association WHERE budgetId = :budgetId")
    suspend fun getBudgetAccountAssociations(budgetId: Int): List<BudgetAccountAssociationEntity>

    @Query("SELECT * FROM budget_account_association WHERE budgetId IN (:budgetIds)")
    suspend fun getBudgetAccountAssociations(budgetIds: List<Int>): List<BudgetAccountAssociationEntity>

    @Query("SELECT * FROM budget_account_association")
    suspend fun getAllBudgetAccountAssociations(): List<BudgetAccountAssociationEntity>


    @Transaction
    suspend fun upsertBudgetsAndAssociations(
        budgets: List<BudgetEntity>,
        associations: List<BudgetAccountAssociationEntity>
    ) {
        upsertBudgets(budgets = budgets)
        upsertBudgetAccountAssociations(associations = associations)
    }

    @Transaction
    suspend fun deleteAndUpsertBudgetsAndAssociations(
        budgetsToDelete: List<BudgetEntity>,
        budgetsToUpsert: List<BudgetEntity>,
        associationsToUpsert: List<BudgetAccountAssociationEntity>,
        associationsToDelete: List<BudgetAccountAssociationEntity>
    ) {
        deleteBudgets(budgets = budgetsToDelete)
        upsertBudgets(budgets = budgetsToUpsert)
        upsertBudgetAccountAssociations(associations = associationsToUpsert)
        deleteBudgetAccountAssociations(associations = associationsToDelete)
    }

}