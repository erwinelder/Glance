package com.ataglance.walletglance.core.domain.app

import com.ataglance.walletglance.account.data.local.model.AccountEntity
import com.ataglance.walletglance.category.data.local.model.CategoryEntity
import com.ataglance.walletglance.record.data.model.RecordEntity
import com.ataglance.walletglance.settings.domain.Settings

data class AppData(
    val records: List<RecordEntity>,
    val accounts: List<AccountEntity>,
    val categories: List<CategoryEntity>,
    val settings: Settings
)