package com.ataglance.walletglance.domain.date

data class DateRangeMenuUiState(
    val startCalendarDateMillis: Long,
    val endCalendarDateMillis: Long,
    val dateRangeWithEnum: DateRangeWithEnum
) {

    fun getLongDateRange(): LongDateRange {
        return dateRangeWithEnum.dateRange
    }

}