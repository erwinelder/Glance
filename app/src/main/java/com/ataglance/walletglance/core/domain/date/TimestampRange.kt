package com.ataglance.walletglance.core.domain.date

import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.presentation.model.ResourceManager
import com.ataglance.walletglance.core.presentation.utils.formatAsDayMonth
import com.ataglance.walletglance.core.presentation.utils.formatTimestampAsDayMonthNameYear
import com.ataglance.walletglance.core.utils.fromTimestamp
import com.ataglance.walletglance.core.utils.getCurrentLocalDateTime
import com.ataglance.walletglance.core.utils.getCurrentTimestamp
import com.ataglance.walletglance.core.utils.toTimestamp
import com.ataglance.walletglance.core.utils.withTime
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.atTime

data class TimestampRange(
    val from: Long,
    val to: Long
) {

    companion object {

        fun fromRange(from: Long?, to: Long?): TimestampRange? {
            if (from == null || to == null) return null
            return TimestampRange(from = from, to = to)
        }

        fun fromLocalDateRange(from: LocalDate, to: LocalDate): TimestampRange {
            return TimestampRange(
                from = from.atTime(hour = 0, minute = 0).toTimestamp(),
                to = to.atTime(hour = 23, minute = 59).toTimestamp()
            )
        }

        fun asToday(): TimestampRange {
            val localDateTime = getCurrentLocalDateTime()
            val from = localDateTime.withTime(hour = 0, minute = 0)
            val to = localDateTime.withTime(hour = 23, minute = 59)

            return TimestampRange(
                from = from.toTimestamp(),
                to = to.toTimestamp()
            )
        }

    }


    fun extendTimeRange(): TimestampRange {
        return TimestampRange(
            from = LocalDateTime.fromTimestamp(from).withTime(hour = 0, minute = 0).toTimestamp(),
            to = LocalDateTime.fromTimestamp(to).withTime(hour = 23, minute = 59).toTimestamp()
        )
    }


    fun equalsTo(other: TimestampRange): Boolean {
        return from == other.from && to == other.to
    }

    fun containsDate(date: Long): Boolean {
        return date in from..to
    }

    fun containsRange(range: TimestampRange): Boolean {
        return from <= range.from && to >= range.to
    }


    fun formatAsDayMonth(): String {
        val fromDate = LocalDate.fromTimestamp(from).formatAsDayMonth()
        val toDate = LocalDate.fromTimestamp(to).formatAsDayMonth()

        return "$fromDate - $toDate"
    }

    fun formatAsDayMonthNameYear(resourceManager: ResourceManager): String {
        return resourceManager.getString(R.string.from_capital) + " " +
                from.formatTimestampAsDayMonthNameYear(resourceManager = resourceManager) + "\n" +
                resourceManager.getString(R.string.to) + " " +
                to.formatTimestampAsDayMonthNameYear(resourceManager = resourceManager)
    }

    fun getCurrentDateAsGraphPercentage(): Float {
        return ((getCurrentTimestamp() - from).toDouble() / (to - from).toDouble()).toFloat()
    }

}