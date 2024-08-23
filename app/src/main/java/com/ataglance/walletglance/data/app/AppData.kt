package com.ataglance.walletglance.data.app

import com.ataglance.walletglance.data.local.entities.AccountEntity
import com.ataglance.walletglance.data.local.entities.CategoryEntity
import com.ataglance.walletglance.data.local.entities.RecordEntity
import com.ataglance.walletglance.data.settings.Settings

data class AppData(
    val records: List<RecordEntity>,
    val accounts: List<AccountEntity>,
    val categories: List<CategoryEntity>,
    val settings: Settings
)