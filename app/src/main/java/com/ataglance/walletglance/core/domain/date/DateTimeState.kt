package com.ataglance.walletglance.core.domain.date

import com.ataglance.walletglance.core.utils.asTimestamp
import com.ataglance.walletglance.core.utils.formatByDefault
import com.ataglance.walletglance.core.utils.getCurrentLocalDateTime
import com.ataglance.walletglance.core.utils.timeInMillisToLocalDateTime
import com.ataglance.walletglance.core.utils.toTimeInMillis
import kotlinx.datetime.LocalDateTime

data class DateTimeState(
    private val localDateTime: LocalDateTime,
    val dateLong: Long,
    val dateFormatted: String
) {

    companion object {

        fun fromCurrentTime(): DateTimeState {
            val dateTime = getCurrentLocalDateTime()

            return DateTimeState(
                localDateTime = dateTime,
                dateLong = dateTime.asTimestamp(),
                dateFormatted = dateTime.formatByDefault()
            )
        }

        fun fromTimestamp(timestamp: Long): DateTimeState {
            val dateSeparated = YearMonthDayHourMinute.fromLong(timestamp)

            val dateTime = LocalDateTime(
                year = dateSeparated.year,
                monthNumber = dateSeparated.month,
                dayOfMonth = dateSeparated.day,
                hour = dateSeparated.hour,
                minute = dateSeparated.minute
            )

            return DateTimeState(
                localDateTime = dateTime,
                dateLong = timestamp,
                dateFormatted = dateTime.formatByDefault()
            )
        }

    }


    fun getTimeInMillis(): Long = localDateTime.toTimeInMillis()

    fun getHourAndMinute(): Pair<Int, Int> = localDateTime.hour to localDateTime.minute

    fun applyNewDate(timeInMillis: Long): DateTimeState {
        val dateTime = timeInMillisToLocalDateTime(timeInMillis).let {
            LocalDateTime(
                year = it.year,
                monthNumber = it.monthNumber,
                dayOfMonth = it.dayOfMonth,
                hour = localDateTime.hour,
                minute = localDateTime.minute
            )
        }

        return DateTimeState(
            localDateTime = dateTime,
            dateLong = dateTime.asTimestamp(),
            dateFormatted = dateTime.formatByDefault()
        )
    }

    fun applyNewTime(hour: Int, minute: Int): DateTimeState {
        val localDateTime = LocalDateTime(
            year = localDateTime.year,
            monthNumber = localDateTime.monthNumber,
            dayOfMonth = localDateTime.dayOfMonth,
            hour = hour,
            minute = minute
        )

        return DateTimeState(
            localDateTime = localDateTime,
            dateLong = localDateTime.asTimestamp(),
            dateFormatted = localDateTime.formatByDefault()
        )
    }

}