package com.ataglance.walletglance.core.utils

import com.ataglance.walletglance.core.domain.date.DateRangeEnum
import com.ataglance.walletglance.core.domain.date.DateRangeWithEnum
import com.ataglance.walletglance.core.data.model.LongDateRange
import com.ataglance.walletglance.core.domain.date.YearMonthDayHourMinute
import java.time.LocalDate

data class LocalDateRange(
    val from: LocalDate,
    val to: LocalDate
) {

    fun toDateRangeState(
        enum: DateRangeEnum
    ): DateRangeWithEnum {
        return DateRangeWithEnum(
            enum = enum,
            dateRange = LongDateRange(from.asTimestamp(), to.asTimestamp() + 2359)
        )
    }

    fun toLongDateRangeWithTime(): LongDateRange {
        return this.let { localDateRange ->
            LongDateRange(
                from = localDateRange.from.let {
                    YearMonthDayHourMinute(it.year, it.monthValue, it.dayOfMonth, 0, 0)
                        .concatenate()
                },
                to = localDateRange.to.let {
                    YearMonthDayHourMinute(it.year, it.monthValue, it.dayOfMonth, 23, 59)
                        .concatenate()
                }
            )
        }
    }

}