package com.ataglance.walletglance.account.domain.mapper

import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.model.RecordAccount


fun Account.toRecordAccount(): RecordAccount {
    return RecordAccount(
        id = id,
        name = name,
        currency = currency,
        color = color
    )
}