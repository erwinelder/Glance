package com.ataglance.walletglance.domain.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.ataglance.walletglance.domain.entities.BudgetEntity

@Dao
interface BudgetDao {

    @Upsert
    suspend fun upsertBudgets(budgetList: List<BudgetEntity>)

    @Query("DELETE FROM Budget WHERE id in (:idList)")
    suspend fun deleteBudgetsByIds(idList: List<Int>)

    @Query("SELECT * FROM Budget")
    suspend fun getAllBudgets(): List<BudgetEntity>

}