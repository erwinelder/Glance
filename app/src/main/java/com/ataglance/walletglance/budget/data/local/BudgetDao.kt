package com.ataglance.walletglance.budget.data.local

import androidx.room.Dao
import androidx.room.Query
import com.ataglance.walletglance.budget.data.model.BudgetEntity
import com.ataglance.walletglance.core.data.local.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao : BaseDao<BudgetEntity> {

    @Query("SELECT * FROM Budget")
    fun getAllBudgets(): Flow<List<BudgetEntity>>

}