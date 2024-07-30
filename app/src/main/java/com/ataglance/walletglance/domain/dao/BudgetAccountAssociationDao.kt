package com.ataglance.walletglance.domain.dao

import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.ataglance.walletglance.domain.entities.BudgetAccountAssociation

interface BudgetAccountAssociationDao {

    @Upsert
    suspend fun upsertAssociations(associationList: List<BudgetAccountAssociation>)

    @Delete
    suspend fun deleteAssociations(associationList: List<BudgetAccountAssociation>)

    @Query("SELECT * FROM BudgetAccountAssociation")
    suspend fun getAllAssociations(): List<BudgetAccountAssociation>

}