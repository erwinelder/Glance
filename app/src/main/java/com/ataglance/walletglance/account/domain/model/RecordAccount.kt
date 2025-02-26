package com.ataglance.walletglance.account.domain.model

import androidx.compose.runtime.Stable
import com.ataglance.walletglance.account.domain.model.color.AccountColors

@Stable
data class RecordAccount(
    val id: Int,
    val name: String,
    val currency: String,
    val color: AccountColors
)
