package com.ataglance.walletglance.record.data.model

data class RecordDataModel(
    val id: Long,
    val date: Long,
    val type: Char,
    val accountId: Int,
    val includeInBudgets: Boolean
)
