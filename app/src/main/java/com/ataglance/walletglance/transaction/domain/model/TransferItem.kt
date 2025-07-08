package com.ataglance.walletglance.transaction.domain.model

data class TransferItem(
    val accountId: Int,
    val amount: Double,
    val rate: Double
)
