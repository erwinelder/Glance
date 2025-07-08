package com.ataglance.walletglance.budget.data.model

data class BudgetDataModel(
    val id: Int,
    val amountLimit: Double,
    val categoryId: Int,
    val name: String,
    val repeatingPeriod: String
)