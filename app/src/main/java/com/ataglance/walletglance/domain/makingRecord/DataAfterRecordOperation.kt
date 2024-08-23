package com.ataglance.walletglance.domain.makingRecord

import com.ataglance.walletglance.domain.budgets.BudgetsByType
import com.ataglance.walletglance.data.local.entities.AccountEntity
import com.ataglance.walletglance.data.local.entities.RecordEntity

data class DataAfterRecordOperation(
    val recordListToDelete: List<RecordEntity> = emptyList(),
    val recordListToUpsert: List<RecordEntity> = emptyList(),
    val accountListToUpsert: List<AccountEntity> = emptyList(),
    val updatedBudgetsByType: BudgetsByType = BudgetsByType()
)
