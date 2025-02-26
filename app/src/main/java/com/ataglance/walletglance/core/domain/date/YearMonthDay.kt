package com.ataglance.walletglance.core.domain.date

import com.ataglance.walletglance.core.utils.toTimeInMillis
import kotlinx.datetime.LocalDate

data class YearMonthDay(
    val year: Int,
    val month: Int,
    val day: Int
) {

    companion object {

        fun fromLong(long: Long): YearMonthDay {
            val year = (long / 100000000).toInt()
            val month = (long / 1000000 - year * 100).toInt()
            val day = (long / 10000 - year * 10000 - month * 100).toInt()
            return YearMonthDay(year, month, day)
        }

    }


    fun concatenate(): Long {
        return year.toLong() * 100000000 + month * 1000000 + day * 10000
    }

    fun getDayWithMonthValueAsString(): String {
        return "$day.$month"
    }

    fun asTimeInMillis(): Long {
        return LocalDate(year, month, day).toTimeInMillis()
    }

}
