package com.ataglance.walletglance.data.app

import com.ataglance.walletglance.domain.entities.Account
import com.ataglance.walletglance.domain.entities.Category
import com.ataglance.walletglance.domain.entities.Record

data class AppData(
    val records: List<Record>,
    val accounts: List<Account>,
    val categories: List<Category>,
    val settings: Settings
)