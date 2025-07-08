package com.ataglance.walletglance.budget.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class BudgetAccountAssociationDto(
    val budgetId: Int,
    val accountId: Int
)
