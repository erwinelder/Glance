package com.ataglance.walletglance.budget.data.local

import androidx.room.Dao
import androidx.room.Query
import com.ataglance.walletglance.budget.data.model.BudgetAccountAssociation
import com.ataglance.walletglance.core.data.local.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetAccountAssociationDao : BaseDao<BudgetAccountAssociation> {

    @Query("SELECT * FROM BudgetAccountAssociation")
    override fun getAllEntities(): Flow<List<BudgetAccountAssociation>>

}