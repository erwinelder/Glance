package com.ataglance.walletglance.budget.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.ataglance.walletglance.budget.data.local.model.BudgetEntity

@Dao
interface BudgetDao {

    @Upsert
    suspend fun upsertBudgets(budgetList: List<BudgetEntity>)

    @Query("DELETE FROM Budget WHERE id IN (:idList)")
    suspend fun deleteBudgetsByIds(idList: List<Int>)

    @Query("SELECT * FROM Budget")
    suspend fun getAllBudgets(): List<BudgetEntity>

}