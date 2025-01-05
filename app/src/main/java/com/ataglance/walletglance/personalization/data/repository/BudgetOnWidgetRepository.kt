package com.ataglance.walletglance.personalization.data.repository

import androidx.room.Transaction
import com.ataglance.walletglance.core.data.repository.BaseEntityRepository
import com.ataglance.walletglance.personalization.data.model.BudgetOnWidgetEntity

interface BudgetOnWidgetRepository : BaseEntityRepository<BudgetOnWidgetEntity> {

    @Transaction
    suspend fun upsertBudgetsOnWidgetAndDeleteOther(budgetsToUpsert: List<BudgetOnWidgetEntity>)

}