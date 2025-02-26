package com.ataglance.walletglance.core.domain.date

import com.ataglance.walletglance.core.utils.toLocalDateRangeByBasicValues
import com.ataglance.walletglance.core.utils.toTimeInMillis
import kotlinx.datetime.atTime

data class DateRangeMenuUiState(
    val startCalendarDateMillis: Long,
    val endCalendarDateMillis: Long,
    val dateRangeWithEnum: DateRangeWithEnum
) {

    companion object {

        fun fromEnum(enum: DateRangeEnum, currentRange: LongDateRange? = null): DateRangeMenuUiState {
            val localDateRange = enum.toLocalDateRangeByBasicValues(dateRange = currentRange)

            return DateRangeMenuUiState(
                startCalendarDateMillis = localDateRange.from.atTime(0, 0).toTimeInMillis(),
                endCalendarDateMillis = localDateRange.to.atTime(23, 59).toTimeInMillis(),
                dateRangeWithEnum = DateRangeWithEnum.fromEnum(enum = enum, currentRange = currentRange)
            )
        }

    }

}