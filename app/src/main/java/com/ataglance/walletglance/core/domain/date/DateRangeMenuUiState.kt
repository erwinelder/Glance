package com.ataglance.walletglance.core.domain.date

import com.ataglance.walletglance.core.data.model.LongDateRange

data class DateRangeMenuUiState(
    val startCalendarDateMillis: Long,
    val endCalendarDateMillis: Long,
    val dateRangeWithEnum: DateRangeWithEnum
) {

    fun getLongDateRange(): LongDateRange {
        return dateRangeWithEnum.dateRange
    }

}