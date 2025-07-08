package com.ataglance.walletglance.transfer.data.model

data class TransferDataModel(
    val id: Long,
    val date: Long,
    val senderAccountId: Int,
    val receiverAccountId: Int,
    val senderAmount: Double,
    val receiverAmount: Double,
    val senderRate: Double,
    val receiverRate: Double,
    val includeInBudgets: Boolean
)
