package com.ataglance.walletglance.domain.accounts

import com.ataglance.walletglance.domain.accounts.color.AccountColorWithName

data class RecordAccount(
    val id: Int,
    val name: String,
    val currency: String,
    val color: AccountColorWithName
)
