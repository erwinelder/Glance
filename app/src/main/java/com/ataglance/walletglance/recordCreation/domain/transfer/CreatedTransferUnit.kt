package com.ataglance.walletglance.recordCreation.domain.transfer

import com.ataglance.walletglance.account.domain.model.Account

data class CreatedTransferUnit(
    val account: Account,
    val recordNum: Int,
    val recordId: Int,
    val amount: Double,
    val rate: Double
)
