package com.ataglance.walletglance.recordCreation.domain

import com.ataglance.walletglance.budget.domain.model.BudgetsByType
import com.ataglance.walletglance.account.data.local.model.AccountEntity
import com.ataglance.walletglance.record.data.model.RecordEntity

data class DataAfterRecordOperation(
    val recordListToDelete: List<RecordEntity> = emptyList(),
    val recordListToUpsert: List<RecordEntity> = emptyList(),
    val accountListToUpsert: List<AccountEntity> = emptyList(),
    val updatedBudgetsByType: BudgetsByType = BudgetsByType()
)
