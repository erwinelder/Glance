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

    companion object {

        fun fromLong(long: Long): YearMonthDayHourMinute {
            val year = (long / 100000000).toInt()
            val month = (long / 1000000 - year * 100).toInt()
            val day = (long / 10000 - year * 10000 - month * 100).toInt()
            val hour = (long / 100 - year * 1000000 - month * 10000 - day * 100).toInt()
            val minute = (long - year * 100000000 - month * 1000000 - day * 10000 - hour * 100).toInt()
            return YearMonthDayHourMinute(year, month, day, hour, minute)
        }

    }


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
