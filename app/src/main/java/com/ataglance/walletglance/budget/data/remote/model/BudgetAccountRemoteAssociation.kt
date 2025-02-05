package com.ataglance.walletglance.budget.data.remote.model

data class BudgetAccountRemoteAssociation(
    val updateTime: Long,
    val deleted: Boolean,
    val budgetId: Int,
    val accountId: Int
)
