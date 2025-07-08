package com.ataglance.walletglance.transaction.presentation.utils

import androidx.annotation.StringRes
import com.ataglance.walletglance.R
import com.ataglance.walletglance.transaction.presentation.model.TransactionTypeFilter


@StringRes
fun TransactionTypeFilter.getNoRecordsMessageRes(): Int {
    return when (this) {
        TransactionTypeFilter.All -> R.string.you_have_no_records_in_date_range
        TransactionTypeFilter.Expenses -> R.string.you_have_no_expenses_in_date_range
        TransactionTypeFilter.Income -> R.string.you_have_no_income_in_date_range
    }
}
