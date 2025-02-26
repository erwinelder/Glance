package com.ataglance.walletglance.budget.data.remote.model

data class BudgetRemoteEntity(
    val updateTime: Long,
    val deleted: Boolean,
    val id: Int,
    val amountLimit: Double,
    val categoryId: Int,
    val name: String,
    val repeatingPeriod: String
)
