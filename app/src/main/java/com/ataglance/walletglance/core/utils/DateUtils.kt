@file:OptIn(ExperimentalTime::class)

package com.ataglance.walletglance.core.utils

import com.ataglance.walletglance.core.domain.date.DateRangeEnum
import com.ataglance.walletglance.core.domain.date.LocalDateRange
import com.ataglance.walletglance.core.domain.date.RepeatingPeriod
import com.ataglance.walletglance.core.domain.date.StringDateRange
import com.ataglance.walletglance.core.domain.date.TimestampRange
import com.ataglance.walletglance.core.domain.date.getDefaultRangesCount
import com.ataglance.walletglance.core.domain.date.getMonthNumber
import com.ataglance.walletglance.core.presentation.model.ResourceManager
import com.ataglance.walletglance.core.presentation.utils.formatByRepeatingPeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant


fun Long.timestampToYear(): Int {
    return LocalDateTime.fromTimestamp(this).year
}
fun Long.timestampToMonth(): Int {
    return LocalDateTime.fromTimestamp(this).month.number
}

fun getCurrentTimestamp(): Long {
    return Clock.System.now().toEpochMilliseconds()
}
fun getCurrentLocalDateTime(): LocalDateTime {
    return Clock.System.now().toLocalDateTime(TimeZone.UTC)
}
fun getCurrentLocalDate(): LocalDate {
    return getCurrentLocalDateTime().date
}


fun LocalDateTime.Companion.fromTimestamp(timestamp: Long): LocalDateTime {
    return Instant.fromEpochMilliseconds(timestamp).toLocalDateTime(TimeZone.UTC)
}
fun LocalDate.Companion.fromTimestamp(timestamp: Long): LocalDate {
    return LocalDateTime.fromTimestamp(timestamp).toLocalDate()
}


fun LocalDateTime.toTimestamp(): Long {
    return toInstant(TimeZone.UTC).toEpochMilliseconds()
}
fun LocalDate.toTimestamp(): Long {
    return atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
}


fun LocalDateTime.atTime(hour: Int = 0, minute: Int = 0, second: Int = 0): LocalDateTime {
    return LocalDateTime(
        year = year,
        month = month.number,
        day = day,
        hour = hour,
        minute = minute,
        second = second
    )
}

fun LocalDateTime.toLocalDate(): LocalDate {
    return LocalDate(year, month.number, day)
}


fun LocalDate.minusDays(days: Int): LocalDate = minus(days, DateTimeUnit.DAY)
fun LocalDate.minusWeeks(weeks: Int): LocalDate = minus(weeks, DateTimeUnit.WEEK)
fun LocalDate.minusMonths(months: Int): LocalDate = minus(months, DateTimeUnit.MONTH)
fun LocalDate.minusYears(years: Int): LocalDate = minus(years, DateTimeUnit.YEAR)
fun LocalDate.plusDays(days: Int): LocalDate = plus(days, DateTimeUnit.DAY)
fun LocalDate.plusWeeks(weeks: Int): LocalDate = plus(weeks, DateTimeUnit.WEEK)
fun LocalDate.plusMonths(months: Int): LocalDate = plus(months, DateTimeUnit.MONTH)
fun LocalDate.plusYears(years: Int): LocalDate = plus(years, DateTimeUnit.YEAR)


fun LocalDateRange.toTimestampRange(): TimestampRange {
    return TimestampRange.fromLocalDateRange(from = from, to = to)
}


fun DateRangeEnum.toLocalDateRangeByBasicValues(
    timestampRange: TimestampRange? = null
): LocalDateRange {
    return when (this) {
        DateRangeEnum.ThisMonth -> LocalDateRange.asThisMonth()
        DateRangeEnum.LastMonth -> LocalDateRange.asLastMonth()
        DateRangeEnum.ThisWeek -> LocalDateRange.asThisWeek()
        DateRangeEnum.SevenDays -> LocalDateRange.asSevenDays()
        DateRangeEnum.ThisYear -> LocalDateRange.asThisYear()
        DateRangeEnum.LastYear -> LocalDateRange.asLastYear()
        else -> getLocalDateRangeByMonth(timestampRange = timestampRange)
    }
}

fun DateRangeEnum.getLocalDateRangeByMonth(timestampRange: TimestampRange?): LocalDateRange {
    val monthNumber = getMonthNumber()

    if (monthNumber == null || timestampRange == null) {
        return LocalDateRange.asThisMonth()
    }

    val firstDay = LocalDate(timestampRange.from.timestampToYear(), monthNumber, 1)
    val lastDay = LocalDate(timestampRange.to.timestampToYear(), monthNumber, 1)
        .plusMonths(1)
        .minusDays(1)

    return LocalDateRange(from = firstDay, to = lastDay)
}


fun RepeatingPeriod.toLocalDateRange(): LocalDateRange {
    return when (this) {
        RepeatingPeriod.Daily -> LocalDateRange.asToday()
        RepeatingPeriod.Weekly -> LocalDateRange.asThisWeek()
        RepeatingPeriod.Monthly -> LocalDateRange.asThisMonth()
        RepeatingPeriod.Yearly -> LocalDateRange.asThisYear()
    }
}

fun RepeatingPeriod.toTimestampRange(): TimestampRange {
    return this.toLocalDateRange().toTimestampRange()
}

fun RepeatingPeriod.getPrevDateRanges(
    range: IntProgression = (getDefaultRangesCount() - 1) downTo 0
): List<TimestampRange> {
    return when (this) {
        RepeatingPeriod.Daily -> range.map {
            LocalDateRange.asDayBefore(it).toTimestampRange()
        }
        RepeatingPeriod.Weekly -> range.map {
            LocalDateRange.asWeekBefore(it).toTimestampRange()
        }
        RepeatingPeriod.Monthly -> range.map {
            LocalDateRange.asMonthBefore(it).toTimestampRange()
        }
        RepeatingPeriod.Yearly -> range.map {
            LocalDateRange.asYearBefore(it).toTimestampRange()
        }
    }
}

fun TimestampRange.toStringDateRange(
    period: RepeatingPeriod,
    resourceManager: ResourceManager
): StringDateRange {
    return StringDateRange(
        from = LocalDateTime.fromTimestamp(from).formatByRepeatingPeriod(
            period = period, resourceManager = resourceManager
        ),
        to = LocalDateTime.fromTimestamp(to).formatByRepeatingPeriod(
            period = period, resourceManager = resourceManager
        )
    )
}
