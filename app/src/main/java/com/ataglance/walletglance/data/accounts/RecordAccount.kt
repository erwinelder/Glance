package com.ataglance.walletglance.data.accounts

import com.ataglance.walletglance.data.accounts.color.AccountColorWithName

data class RecordAccount(
    val id: Int,
    val name: String,
    val currency: String,
    val color: AccountColorWithName
)
