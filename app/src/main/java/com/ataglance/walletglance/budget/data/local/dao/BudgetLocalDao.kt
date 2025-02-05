package com.ataglance.walletglance.budget.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.ataglance.walletglance.budget.data.local.model.BudgetAccountAssociation
import com.ataglance.walletglance.budget.data.local.model.BudgetEntity

@Dao
interface BudgetLocalDao {

    @Upsert
    suspend fun upsertBudgets(budgets: List<BudgetEntity>)

    @Delete
    suspend fun deleteBudgets(budgets: List<BudgetEntity>)

    @Transaction
    suspend fun deleteAndUpsertBudgets(toDelete: List<BudgetEntity>, toUpsert: List<BudgetEntity>) {
        deleteBudgets(toDelete)
        upsertBudgets(toUpsert)
    }

    @Query("SELECT * FROM Budget")
    suspend fun getAllBudgets(): List<BudgetEntity>


    @Upsert
    suspend fun upsertBudgetAccountAssociations(associations: List<BudgetAccountAssociation>)

    @Delete
    suspend fun deleteBudgetAccountAssociations(associations: List<BudgetAccountAssociation>)

    @Transaction
    suspend fun deleteAndUpsertBudgetAccountAssociations(
        toDelete: List<BudgetAccountAssociation>,
        toUpsert: List<BudgetAccountAssociation>
    ) {
        deleteBudgetAccountAssociations(toDelete)
        upsertBudgetAccountAssociations(toUpsert)
    }

    @Query("SELECT * FROM BudgetAccountAssociation")
    suspend fun getAllBudgetAccountAssociations(): List<BudgetAccountAssociation>


    @Transaction
    suspend fun deleteAndUpsertBudgetsAndAssociations(
        budgetsToDelete: List<BudgetEntity>,
        budgetsToUpsert: List<BudgetEntity>,
        associationsToDelete: List<BudgetAccountAssociation>,
        associationsToUpsert: List<BudgetAccountAssociation>
    ) {
        deleteBudgets(budgetsToDelete)
        upsertBudgets(budgetsToUpsert)
        deleteBudgetAccountAssociations(associationsToDelete)
        upsertBudgetAccountAssociations(associationsToUpsert)
    }

}