package com.ataglance.walletglance.account.data.remote.model

data class AccountRemoteEntity(
    val updateTime: Long,
    val deleted: Boolean,
    val id: Int,
    val orderNum: Int,
    val name: String,
    val currency: String,
    val balance: Double,
    val color: String,
    val hide: Boolean,
    val hideBalance: Boolean,
    val withoutBalance: Boolean
)
