package com.ataglance.walletglance.budget.presentation.model

import com.ataglance.walletglance.budget.domain.model.Budget

data class CheckedBudget(
    val checked: Boolean,
    val budget: Budget
)
