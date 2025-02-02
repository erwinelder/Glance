package com.ataglance.walletglance.account.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ataglance.walletglance.account.domain.model.color.AccountColorName

@Entity(tableName = "Account")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val orderNum: Int = 0,
    val name: String = "USD",
    val currency: String = "USD",
    val balance: Double = 0.0,
    val color: String = AccountColorName.Default.name,
    val hide: Boolean = false,
    val hideBalance: Boolean = false,
    val withoutBalance: Boolean = false,
    val isActive: Boolean = true
)