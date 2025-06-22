package com.ataglance.walletglance.core.domain.date

import com.ataglance.walletglance.core.presentation.utils.formatByDefault
import com.ataglance.walletglance.core.utils.fromTimestamp
import com.ataglance.walletglance.core.utils.getCurrentLocalDateTime
import com.ataglance.walletglance.core.utils.toTimestamp
import com.ataglance.walletglance.core.utils.withTime
import kotlinx.datetime.LocalDateTime

data class DateTimeState(
    private val localDateTime: LocalDateTime,
    val timestamp: Long,
    val dateFormatted: String
) {

    companion object {

        fun fromCurrentTime(): DateTimeState {
            val dateTime = getCurrentLocalDateTime()

            return DateTimeState(
                localDateTime = dateTime,
                timestamp = dateTime.toTimestamp(),
                dateFormatted = dateTime.formatByDefault()
            )
        }

        fun fromTimestamp(timestamp: Long): DateTimeState {
            val localDateTime = LocalDateTime.fromTimestamp(timestamp)

            return DateTimeState(
                localDateTime = localDateTime,
                timestamp = timestamp,
                dateFormatted = localDateTime.formatByDefault()
            )
        }

    }


    fun getHourAndMinute(): Pair<Int, Int> = localDateTime.hour to localDateTime.minute

    fun applyNewDate(newTimestamp: Long): DateTimeState {
        val newLocalDateTime = LocalDateTime.fromTimestamp(newTimestamp).withTime(
            hour = localDateTime.hour, minute = localDateTime.minute
        )

        return DateTimeState(
            localDateTime = newLocalDateTime,
            timestamp = newTimestamp,
            dateFormatted = newLocalDateTime.formatByDefault()
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
            timestamp = localDateTime.toTimestamp(),
            dateFormatted = localDateTime.formatByDefault()
        )
    }

}