package com.ataglance.walletglance.transfer.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class TransferCommandDto(
    val id: Long,
    val date: Long,
    val senderAccountId: Int,
    val receiverAccountId: Int,
    val senderAmount: Double,
    val receiverAmount: Double,
    val senderRate: Double,
    val receiverRate: Double,
    val includeInBudgets: Boolean,
    val timestamp: Long,
    val deleted: Boolean
)
