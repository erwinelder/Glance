package com.ataglance.walletglance.model

import com.ataglance.walletglance.data.Account
import com.ataglance.walletglance.data.Category
import com.ataglance.walletglance.data.Record
import com.ataglance.walletglance.data.Settings

data class AppData(
    val records: List<Record>,
    val accounts: List<Account>,
    val categories: List<Category>,
    val settings: Settings
)