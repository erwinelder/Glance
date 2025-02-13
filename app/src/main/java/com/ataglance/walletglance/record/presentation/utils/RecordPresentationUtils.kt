package com.ataglance.walletglance.record.presentation.utils

import androidx.annotation.StringRes
import com.ataglance.walletglance.R
import com.ataglance.walletglance.record.domain.model.RecordsTypeFilter


@StringRes
fun RecordsTypeFilter.getNoRecordsMessageRes(): Int {
    return when (this) {
        RecordsTypeFilter.All -> R.string.you_have_no_records_in_date_range
        RecordsTypeFilter.Expenses -> R.string.you_have_no_expenses_in_date_range
        RecordsTypeFilter.Income -> R.string.you_have_no_income_in_date_range
    }
}
