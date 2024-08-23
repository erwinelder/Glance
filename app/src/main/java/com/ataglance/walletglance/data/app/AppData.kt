package com.ataglance.walletglance.data.app

import com.ataglance.walletglance.data.settings.Settings
import com.ataglance.walletglance.data.local.entities.AccountEntity
import com.ataglance.walletglance.data.local.entities.CategoryEntity
import com.ataglance.walletglance.data.local.entities.Record

data class AppData(
    val records: List<Record>,
    val accounts: List<AccountEntity>,
    val categories: List<CategoryEntity>,
    val settings: Settings
)