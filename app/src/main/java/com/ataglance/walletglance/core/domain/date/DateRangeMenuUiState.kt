package com.ataglance.walletglance.core.domain.date

data class DateRangeMenuUiState(
    val startCalendarDateMillis: Long,
    val endCalendarDateMillis: Long,
    val dateRangeWithEnum: DateRangeWithEnum
) {

    fun getLongDateRange(): LongDateRange {
        return dateRangeWithEnum.dateRange
    }

}