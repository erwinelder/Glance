package com.ataglance.walletglance.core.domain.date

import com.ataglance.walletglance.core.utils.month
import java.util.Calendar

data class YearMonthDayHourMinute(
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int = 0,
    val minute: Int = 0
) {

    fun concatenate(): Long {
        return year.toLong() * 100000000 + month * 1000000 + day * 10000 + hour * 100 + minute
    }

    fun asTimeInMillis(): Long {
        Calendar.getInstance().apply {
            set(year, month, day, hour, minute)
            return timeInMillis
        }
    }

}
