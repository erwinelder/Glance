package com.ataglance.walletglance.budget.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class BudgetOnWidgetDto(
    val budgetId: Int,
    val timestamp: Long,
    val deleted: Boolean
)
