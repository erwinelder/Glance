package com.ataglance.walletglance.data.app

import com.ataglance.walletglance.data.budgets.BudgetsByType
import com.ataglance.walletglance.domain.entities.AccountEntity
import com.ataglance.walletglance.domain.entities.Record

data class DataAfterRecordOperation(
    val recordListToDelete: List<Record> = emptyList(),
    val recordListToUpsert: List<Record> = emptyList(),
    val accountListToUpsert: List<AccountEntity> = emptyList(),
    val updatedBudgetsByType: BudgetsByType = BudgetsByType()
)
