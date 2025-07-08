package com.ataglance.walletglance.account.data.model

data class AccountDataModel(
    val id: Int = 0,
    val orderNum: Int,
    val name: String,
    val currency: String,
    val balance: Double,
    val color: String,
    val hide: Boolean,
    val hideBalance: Boolean,
    val withoutBalance: Boolean
)