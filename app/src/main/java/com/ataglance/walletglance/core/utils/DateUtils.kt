package com.ataglance.walletglance.core.utils

import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.date.DateRangeEnum
import com.ataglance.walletglance.core.domain.date.LocalDateRange
import com.ataglance.walletglance.core.domain.date.LongDateRange
import com.ataglance.walletglance.core.domain.date.RepeatingPeriod
import com.ataglance.walletglance.core.domain.date.StringDateRange
import com.ataglance.walletglance.core.domain.date.TimeInMillisRange
import com.ataglance.walletglance.core.domain.date.YearMonthDay
import com.ataglance.walletglance.core.domain.date.YearMonthDayHourMinute
import com.ataglance.walletglance.core.presentation.model.ResourceManager
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime


fun Long.extractYear(): Int {
    return (this / 100000000).toInt()
}

fun Long.extractMonth(): Int {
    return (this / 1000000 - this.extractYear() * 100).toInt()
}


fun LocalDate.minusDays(days: Int): LocalDate {
    return minus(days, DateTimeUnit.DAY)
}
fun LocalDate.minusWeeks(weeks: Int): LocalDate {
    return minus(weeks, DateTimeUnit.WEEK)
}
fun LocalDate.minusMonths(months: Int): LocalDate {
    return minus(months, DateTimeUnit.MONTH)
}
fun LocalDate.minusYears(years: Int): LocalDate {
    return minus(years, DateTimeUnit.YEAR)
}
fun LocalDate.plusDays(days: Int): LocalDate {
    return plus(days, DateTimeUnit.DAY)
}
fun LocalDate.plusWeeks(weeks: Int): LocalDate {
    return plus(weeks, DateTimeUnit.WEEK)
}
fun LocalDate.plusMonths(months: Int): LocalDate {
    return plus(months, DateTimeUnit.MONTH)
}
fun LocalDate.plusYears(years: Int): LocalDate {
    return plus(years, DateTimeUnit.YEAR)
}


fun timeInMillisToLocalDateTime(timeInMillis: Long): LocalDateTime {
    return Instant.fromEpochMilliseconds(timeInMillis).toLocalDateTime(TimeZone.UTC)
}

fun timeInMillisToLocalDate(timeInMillis: Long): LocalDate {
    return timeInMillisToLocalDateTime(timeInMillis).toLocalDate()
}

fun timeInMillisToTimestampWithoutSpecificTime(timeInMillis: Long): Long {
    return timeInMillisToLocalDate(timeInMillis).asTimestamp()
}


fun LocalDate.toTimeInMillis(): Long {
    return this.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
}
fun LocalDateTime.toTimeInMillis(): Long {
    return this.toInstant(TimeZone.UTC).toEpochMilliseconds()
}

fun LongDateRange.asTimeInMillisRange(): TimeInMillisRange {
    return TimeInMillisRange(
        from = YearMonthDayHourMinute.fromLong(from).asTimeInMillis(),
        to = YearMonthDayHourMinute.fromLong(to).asTimeInMillis()
    )
}

fun getCurrentTimeInMillis(): Long {
    return Clock.System.now().toEpochMilliseconds()
}


fun getCurrentLocalDateTime(): LocalDateTime {
    return Clock.System.now().toLocalDateTime(TimeZone.UTC)
}
fun getCurrentLocalDate(): LocalDate {
    return getCurrentLocalDateTime().date
}

fun getCurrentTimestamp(): Long {
    return getCurrentLocalDateTime().asTimestamp()
}
fun getCurrentEpochTimestamp(): Long {
    return getCurrentLocalDateTime().toInstant(TimeZone.UTC).epochSeconds
}

fun getCurrentDateLong(): Long {
    return getCurrentLocalDate().asTimestamp()
}


fun LocalDate.asTimestamp(): Long {
    return YearMonthDay(this.year, this.monthNumber, this.dayOfMonth).concatenate()
}
fun LocalDateTime.asTimestamp(): Long {
    return YearMonthDayHourMinute(
        this.year, this.monthNumber, this.dayOfMonth, this.hour, this.minute
    ).concatenate()
}


fun LocalDateTime.toLocalDate(): LocalDate {
    return LocalDate(this.year, this.monthNumber, this.dayOfMonth)
}


fun DateRangeEnum.toLocalDateRangeByBasicValues(dateRange: LongDateRange? = null): LocalDateRange {
    return when (this) {
        DateRangeEnum.ThisMonth -> LocalDateRange.asThisMonth()
        DateRangeEnum.LastMonth -> LocalDateRange.asLastMonth()
        DateRangeEnum.ThisWeek -> LocalDateRange.asThisWeek()
        DateRangeEnum.SevenDays -> LocalDateRange.asSevenDays()
        DateRangeEnum.ThisYear -> LocalDateRange.asThisYear()
        DateRangeEnum.LastYear -> LocalDateRange.asLastYear()
        else -> this.getLocalDateRangeByMonth(dateRange = dateRange)
    }
}

fun DateRangeEnum.getLocalDateRangeByMonth(dateRange: LongDateRange?): LocalDateRange {
    val monthNumber = this.getMonthNumber()

    if (monthNumber == null || dateRange == null) {
        return LocalDateRange.asThisMonth()
    }

    val firstDay = LocalDate(dateRange.from.extractYear(), monthNumber, 1)
    val lastDay = LocalDate(dateRange.to.extractYear(), monthNumber, 1).plusMonths(1).minusDays(1)

    return LocalDateRange(firstDay, lastDay)
}


fun DateRangeEnum.getMonthNumber(): Int? {
    return when (this) {
        DateRangeEnum.January -> 1
        DateRangeEnum.February -> 2
        DateRangeEnum.March -> 3
        DateRangeEnum.April -> 4
        DateRangeEnum.May -> 5
        DateRangeEnum.June -> 6
        DateRangeEnum.July -> 7
        DateRangeEnum.August -> 8
        DateRangeEnum.September -> 9
        DateRangeEnum.October -> 10
        DateRangeEnum.November -> 11
        DateRangeEnum.December -> 12
        else -> null
    }
}

fun DateRangeEnum.getMonthFullNameRes(): Int? {
    return when (this) {
        DateRangeEnum.January -> R.string.january_full
        DateRangeEnum.February -> R.string.february_full
        DateRangeEnum.March -> R.string.march_full
        DateRangeEnum.April -> R.string.april_full
        DateRangeEnum.May -> R.string.may_full
        DateRangeEnum.June -> R.string.june_full
        DateRangeEnum.July -> R.string.july_full
        DateRangeEnum.August -> R.string.august_full
        DateRangeEnum.September -> R.string.september_full
        DateRangeEnum.October -> R.string.october_full
        DateRangeEnum.November -> R.string.november_full
        DateRangeEnum.December -> R.string.december_full
        else -> null
    }
}

private fun Int.getMonthShortNameRes(): Int? {
    return when (this) {
        1 -> R.string.january_short
        2 -> R.string.february_short
        3 -> R.string.march_short
        4 -> R.string.april_short
        5 -> R.string.may_short
        6 -> R.string.june_short
        7 -> R.string.july_short
        8 -> R.string.august_short
        9 -> R.string.september_short
        10 -> R.string.october_short
        11 -> R.string.november_short
        12 -> R.string.december_short
        else -> null
    }
}


fun LocalDateTime.formatByDefault(): String {
    return this.format(
        format = LocalDateTime.Format {
            dayOfMonth(); char('.'); monthNumber(); char('.'); year()
            char(' ')
            hour(); char(':'); minute()
        }
    )
}


fun RepeatingPeriod.asStringRes(): Int {
    return when (this) {
        RepeatingPeriod.Daily -> R.string.daily
        RepeatingPeriod.Weekly -> R.string.weekly
        RepeatingPeriod.Monthly -> R.string.monthly
        RepeatingPeriod.Yearly -> R.string.yearly
    }
}

fun RepeatingPeriod.getSpendingInRecentStringRes(): Int {
    return when (this) {
        RepeatingPeriod.Daily -> R.string.spending_in_recent_days_with_currency
        RepeatingPeriod.Weekly -> R.string.spending_in_recent_weeks_with_currency
        RepeatingPeriod.Monthly -> R.string.spending_in_recent_months_with_currency
        RepeatingPeriod.Yearly -> R.string.spending_in_recent_years_with_currency
    }
}

fun RepeatingPeriod.getLongDateRangeWithTime(): LongDateRange {
    return when (this) {
        RepeatingPeriod.Daily -> LocalDateRange.asToday().toLongDateRange()
        RepeatingPeriod.Weekly -> LocalDateRange.asThisWeek().toLongDateRange()
        RepeatingPeriod.Monthly -> LocalDateRange.asThisMonth().toLongDateRange()
        RepeatingPeriod.Yearly -> LocalDateRange.asThisYear().toLongDateRange()
    }
}


fun RepeatingPeriod.getPrevDateRanges(
    range: IntProgression = (this.getDefaultRangesCount() - 1) downTo 0
): List<LongDateRange> {
    return when (this) {
        RepeatingPeriod.Daily -> range.map {
            LocalDateRange.asDayBefore(it).toLocalDateTimeRange().toLongDateRange()
        }
        RepeatingPeriod.Weekly -> range.map {
            LocalDateRange.asWeekBefore(it).toLocalDateTimeRange().toLongDateRange()
        }
        RepeatingPeriod.Monthly -> range.map {
            LocalDateRange.asMonthBefore(it).toLocalDateTimeRange().toLongDateRange()
        }
        RepeatingPeriod.Yearly -> range.map {
            LocalDateRange.asYearBefore(it).toLocalDateTimeRange().toLongDateRange()
        }
    }
}

private fun RepeatingPeriod.getDefaultRangesCount(): Int {
    return when (this) {
        RepeatingPeriod.Daily -> 7
        RepeatingPeriod.Weekly -> 4
        RepeatingPeriod.Monthly -> 6
        RepeatingPeriod.Yearly -> 3
    }
}


fun Int.getGreetingsWidgetTitleRes(): Int {
    return when (this) {
        in 6..11 -> R.string.greetings_title_morning
        in 12..17 -> R.string.greetings_title_afternoon
        in 18..22 -> R.string.greetings_title_evening
        else -> R.string.greetings_title_night
    }
}


fun RepeatingPeriod.getColumnNameForColumnChart(
    dateRange: LongDateRange,
    resourceManager: ResourceManager
): String {
    return when (this) {
        RepeatingPeriod.Daily -> YearMonthDay.fromLong(dateRange.from).getDayWithMonthValueAsString()
        RepeatingPeriod.Weekly -> dateRange.getDayWithMonthValueRangeAsString()
        RepeatingPeriod.Monthly -> resourceManager.getString(
            id = dateRange.from.extractMonth().getMonthShortNameRes()
        )
        RepeatingPeriod.Yearly -> dateRange.from.extractYear().toString()
    }
}


fun Long.formatDateLongAsDayMonthYear(
    resourceManager: ResourceManager,
    includeYear: Boolean = true
): String {
    val yearMonthDay = YearMonthDay.fromLong(this)
    val monthString = resourceManager.getString(id = yearMonthDay.month.getMonthShortNameRes())

    return if (includeYear) {
        "${yearMonthDay.day} $monthString ${yearMonthDay.year}"
    } else {
        "${yearMonthDay.day} $monthString"
    }
}


fun LongDateRange.toStringDateRange(
    period: RepeatingPeriod,
    resourceManager: ResourceManager
): StringDateRange {
    return StringDateRange(
        from = YearMonthDayHourMinute.fromLong(from).formatByRepeatingPeriod(period, resourceManager),
        to = YearMonthDayHourMinute.fromLong(to).formatByRepeatingPeriod(period, resourceManager)
    )
}

fun YearMonthDayHourMinute.formatByRepeatingPeriod(
    repeatingPeriod: RepeatingPeriod,
    resourceManager: ResourceManager
): String {
    return when (repeatingPeriod) {
        RepeatingPeriod.Daily -> "${hour}:${minute}"
        RepeatingPeriod.Weekly, RepeatingPeriod.Monthly -> "$day " +
                resourceManager.getString(month.getMonthShortNameRes())
        RepeatingPeriod.Yearly -> resourceManager.getString(month.getMonthShortNameRes()) + " $year"
    }
}
