package com.ataglance.walletglance.account.domain

import androidx.compose.runtime.Stable
import com.ataglance.walletglance.account.domain.color.AccountColorWithName

@Stable
data class RecordAccount(
    val id: Int,
    val name: String,
    val currency: String,
    val color: AccountColorWithName
)
