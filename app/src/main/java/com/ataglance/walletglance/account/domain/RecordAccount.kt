package com.ataglance.walletglance.account.domain

import com.ataglance.walletglance.account.domain.color.AccountColorWithName

data class RecordAccount(
    val id: Int,
    val name: String,
    val currency: String,
    val color: AccountColorWithName
)
