package com.ataglance.walletglance.account.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val orderNum: Int,
    val name: String,
    val currency: String,
    val balance: Double,
    val color: String,
    val hide: Boolean,
    val hideBalance: Boolean,
    val withoutBalance: Boolean,
    val timestamp: Long
)