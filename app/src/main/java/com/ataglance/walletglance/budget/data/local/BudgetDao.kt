package com.ataglance.walletglance.budget.data.local

import androidx.room.Dao
import androidx.room.Query
import com.ataglance.walletglance.budget.data.model.BudgetEntity
import com.ataglance.walletglance.core.data.local.dao.BaseLocalDao
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao : BaseLocalDao<BudgetEntity> {

    @Query("SELECT * FROM Budget")
    fun getAllBudgets(): Flow<List<BudgetEntity>>

    @Query("SELECT * FROM Budget")
    override fun getAllEntities(): Flow<List<BudgetEntity>>

}