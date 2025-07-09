package com.ataglance.walletglance.core.domain.date

import com.ataglance.walletglance.core.presentation.utils.formatByDefault
import com.ataglance.walletglance.core.utils.fromTimestamp
import com.ataglance.walletglance.core.utils.getCurrentLocalDateTime
import com.ataglance.walletglance.core.utils.toTimestamp
import com.ataglance.walletglance.core.utils.atTime
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.number

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

    fun applyNewDate(timestamp: Long): DateTimeState {
        val newLocalDateTime = LocalDateTime.fromTimestamp(timestamp).atTime(
            hour = localDateTime.hour, minute = localDateTime.minute
        )

        return DateTimeState(
            localDateTime = newLocalDateTime,
            timestamp = timestamp,
            dateFormatted = newLocalDateTime.formatByDefault()
        )
    }

    fun applyNewTime(hour: Int, minute: Int): DateTimeState {
        val localDateTime = LocalDateTime(
            year = localDateTime.year,
            month = localDateTime.month.number,
            day = localDateTime.day,
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