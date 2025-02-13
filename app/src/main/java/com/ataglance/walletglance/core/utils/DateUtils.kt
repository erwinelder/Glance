package com.ataglance.walletglance.core.utils

import android.content.Context
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.LongRange
import com.ataglance.walletglance.core.domain.date.DateRangeEnum
import com.ataglance.walletglance.core.domain.date.DateRangeMenuUiState
import com.ataglance.walletglance.core.domain.date.DateRangeWithEnum
import com.ataglance.walletglance.core.domain.date.DateTimeState
import com.ataglance.walletglance.core.domain.date.LongDateRange
import com.ataglance.walletglance.core.domain.date.RepeatingPeriod
import com.ataglance.walletglance.core.domain.date.StringDateRange
import com.ataglance.walletglance.core.domain.date.TimeInMillisRange
import com.ataglance.walletglance.core.domain.date.YearMonthDay
import com.ataglance.walletglance.core.domain.date.YearMonthDayHourMinute
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale


val Calendar.year: Int
    get() {
        return this.get(Calendar.YEAR)
    }

val Calendar.month: Int
    get() {
        return this.get(Calendar.MONTH)
    }

val Calendar.day: Int
    get() {
        return this.get(Calendar.DAY_OF_MONTH)
    }

val Calendar.hour: Int
    get() {
        return this.get(Calendar.HOUR_OF_DAY)
    }

val Calendar.minute: Int
    get() {
        return this.get(Calendar.MINUTE)
    }
fun Calendar.toLongWithTime(): Long {
    return YearMonthDayHourMinute(
        this.year, this.month + 1, this.day, this.hour, this.minute
    ).concatenate()
}



fun Calendar.getFormattedDateWithTime(): String {
    val localDateTime = LocalDateTime.now()
        .withYear(this.year)
        .withMonth(this.month + 1)
        .withDayOfMonth(this.day)
        .withHour(this.hour)
        .withMinute(this.minute)
    return localDateTime.format(DateTimeFormatter.ofPattern("dd.MM.uuuu, HH:mm"))
}



fun LocalDate.asTimestamp(): Long {
    return YearMonthDay(this.year, this.monthValue, this.dayOfMonth).concatenate()
}

fun LocalDateTime.asTimestamp(): Long {
    return YearMonthDayHourMinute(
        this.year, this.monthValue, this.dayOfMonth, this.hour, this.minute
    ).concatenate()
}



fun LocalDate.toTimeInMillis(): Long {
    val calendar = Calendar.getInstance()
    calendar.set(this.year, this.monthValue - 1, this.dayOfMonth)
    return calendar.timeInMillis
}



private fun getThisWeekLocalDateRange(): LocalDateRange {
    val today = LocalDate.now()
    val monday = today.minusDays(today.dayOfWeek.value.toLong() - 1)
    val sunday = monday.plusDays(6)

    return LocalDateRange(monday, sunday)
}


private fun getSevenDaysLocalDateRange(): LocalDateRange {
    val today = LocalDate.now()
    val sixDaysBefore = today.minusDays(6)

    return LocalDateRange(sixDaysBefore, today)
}


private fun getThisMonthLocalDateRange(): LocalDateRange {
    val today = LocalDate.now()
    val firstDay = today.withDayOfMonth(1)
    val lastDay = today.withDayOfMonth(1).plusMonths(1).minusDays(1)

    return LocalDateRange(firstDay, lastDay)
}


private fun getLastMonthLocalDateRange(): LocalDateRange {
    val today = LocalDate.now()
    val firstDay = today.minusMonths(1).withDayOfMonth(1)
    val lastDay = today.withDayOfMonth(1).minusDays(1)

    return LocalDateRange(firstDay, lastDay)
}


private fun getThisYearLocalDateRange(): LocalDateRange {
    val today = LocalDate.now()
    val firstDayOfYear = today.withMonth(1).withDayOfMonth(1)
    val lastDayOfYear = today.withMonth(12).withDayOfMonth(31)

    return LocalDateRange(firstDayOfYear, lastDayOfYear)
}


private fun getLastYearLocalDateRange(): LocalDateRange {
    val today = LocalDate.now()
    val firstDayOfYear = today.minusYears(1).withMonth(1).withDayOfMonth(1)
    val lastDayOfYear = today.minusYears(1).withMonth(12).withDayOfMonth(31)

    return LocalDateRange(firstDayOfYear, lastDayOfYear)
}


private fun getLocalDateRangeBySpecificMonth(
    month: DateRangeEnum, currentDateRangeWithEnum: DateRangeWithEnum?
): LocalDateRange {
    val monthValue = month.getMonthValue()

    if (monthValue == null || currentDateRangeWithEnum == null) {
        return getThisMonthLocalDateRange()
    }

    val today = LocalDate.now()
    val firstDay = today
        .withYear(currentDateRangeWithEnum.dateRange.from.extractYear())
        .withMonth(monthValue)
        .withDayOfMonth(1)
    val lastDay = today
        .withYear(currentDateRangeWithEnum.dateRange.to.extractYear())
        .withMonth(monthValue)
        .plusMonths(1)
        .withDayOfMonth(1)
        .minusDays(1)

    return LocalDateRange(firstDay, lastDay)
}


fun DateRangeEnum.withLongDateRange(
    currentDateRangeWithEnum: DateRangeWithEnum? = null
): DateRangeWithEnum {
    return when (this) {
        DateRangeEnum.ThisWeek ->
            getThisWeekLocalDateRange().toDateRangeState(DateRangeEnum.ThisWeek)
        DateRangeEnum.SevenDays ->
            getSevenDaysLocalDateRange().toDateRangeState(DateRangeEnum.SevenDays)
        DateRangeEnum.ThisMonth ->
            getThisMonthLocalDateRange().toDateRangeState(DateRangeEnum.ThisMonth)
        DateRangeEnum.LastMonth ->
            getLastMonthLocalDateRange().toDateRangeState(DateRangeEnum.LastMonth)
        DateRangeEnum.ThisYear ->
            getThisYearLocalDateRange().toDateRangeState(DateRangeEnum.ThisYear)
        DateRangeEnum.LastYear ->
            getLastYearLocalDateRange().toDateRangeState(DateRangeEnum.LastYear)
        else -> getLocalDateRangeBySpecificMonth(this, currentDateRangeWithEnum)
            .toDateRangeState(this)
    }
}


private fun DateRangeEnum.getMonthValue(): Int? {
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


fun Int.getMonthShortNameRes(): Int? {
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


fun Long.asOneDayDateRange(): LongDateRange {
    val bottomBorder = this / 100 * 100
    return LongDateRange(bottomBorder, bottomBorder + 2359)
}


fun getTodayDateLong(): Long {
    return LocalDate.now().asTimestamp()
}

fun getCurrentTimestamp(): Long {
    return LocalDateTime.now().asTimestamp()
}


fun getTodayLongDateRange(): LongDateRange {
    return getTodayDateLong().asOneDayDateRange()
}


fun DateRangeEnum.getCalendarStartLong(currentDateRangeWithEnum: DateRangeWithEnum? = null): Long {
    return when(this) {
        DateRangeEnum.ThisMonth -> getThisMonthLocalDateRange().from.toTimeInMillis()
        DateRangeEnum.LastMonth -> getLastMonthLocalDateRange().from.toTimeInMillis()
        DateRangeEnum.ThisWeek -> getThisWeekLocalDateRange().from.toTimeInMillis()
        DateRangeEnum.SevenDays -> getSevenDaysLocalDateRange().from.toTimeInMillis()
        DateRangeEnum.ThisYear -> getThisYearLocalDateRange().from.toTimeInMillis()
        DateRangeEnum.LastYear -> getLastYearLocalDateRange().from.toTimeInMillis()
        else -> getLocalDateRangeBySpecificMonth(this, currentDateRangeWithEnum)
            .from.toTimeInMillis()
    }
}


fun DateRangeEnum.getCalendarEndLong(currentDateRangeWithEnum: DateRangeWithEnum? = null): Long {
    return when(this) {
        DateRangeEnum.ThisMonth -> getThisMonthLocalDateRange().to.toTimeInMillis()
        DateRangeEnum.LastMonth -> getLastMonthLocalDateRange().to.toTimeInMillis()
        DateRangeEnum.ThisWeek -> getThisWeekLocalDateRange().to.toTimeInMillis()
        DateRangeEnum.SevenDays -> getSevenDaysLocalDateRange().to.toTimeInMillis()
        DateRangeEnum.ThisYear -> getThisYearLocalDateRange().to.toTimeInMillis()
        DateRangeEnum.LastYear -> getLastYearLocalDateRange().to.toTimeInMillis()
        else -> getLocalDateRangeBySpecificMonth(this, currentDateRangeWithEnum).to
            .toTimeInMillis()
    }
}


fun DateRangeEnum.getDateRangeMenuUiState(): DateRangeMenuUiState {
    return DateRangeMenuUiState(
        startCalendarDateMillis = this.getCalendarStartLong(),
        endCalendarDateMillis = this.getCalendarEndLong(),
        dateRangeWithEnum = this.withLongDateRange()
    )
}


fun getRepeatingPeriodByString(periodValue: String): RepeatingPeriod? {
    return when (periodValue) {
        RepeatingPeriod.Daily.name -> RepeatingPeriod.Daily
        RepeatingPeriod.Weekly.name -> RepeatingPeriod.Weekly
        RepeatingPeriod.Monthly.name -> RepeatingPeriod.Monthly
        RepeatingPeriod.Yearly.name -> RepeatingPeriod.Yearly
        else -> null
    }
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
        RepeatingPeriod.Daily -> getTodayLongDateRange()
        RepeatingPeriod.Weekly -> getThisWeekLocalDateRange().toLongDateRangeWithTime()
        RepeatingPeriod.Monthly -> getThisMonthLocalDateRange().toLongDateRangeWithTime()
        RepeatingPeriod.Yearly -> getThisYearLocalDateRange().toLongDateRangeWithTime()
    }
}


fun RepeatingPeriod.getPrevDateRanges(
    topRangeIndex: Long = 1,
    lowRangeIndex: Long = this.getDefaultRangesCount() - 1L
): List<LongDateRange> {
    val stepsRange = LongRange(topRangeIndex, lowRangeIndex)
    return when (this) {
        RepeatingPeriod.Daily -> getLastDateRanges(
            lastDatesRange = stepsRange,
            bottomDateBorderTransformation = { today, i ->
                today.minusDays(i).withHour(0).withMinute(0)
            },
            topDateBorderTransformation = { today, i ->
                today.minusDays(i).withHour(23).withMinute(59)
            }
        )
        RepeatingPeriod.Weekly -> getLastDateRanges(
            lastDatesRange = stepsRange,
            bottomDateBorderTransformation = { today, i ->
                today.minusDays(today.dayOfWeek.value - 1L).minusWeeks(i).withHour(0).withMinute(0)
            },
            topDateBorderTransformation = { today, i ->
                today.plusDays(7L - today.dayOfWeek.value).minusWeeks(i).withHour(23).withMinute(59)
            }
        )
        RepeatingPeriod.Monthly -> getLastDateRanges(
            lastDatesRange = stepsRange,
            bottomDateBorderTransformation = { today, i ->
                today.minusMonths(i).withDayOfMonth(1).withHour(0).withMinute(0)
            },
            topDateBorderTransformation = { today, i ->
                today.minusMonths(i - 1).withDayOfMonth(1).minusDays(1).withHour(23).withMinute(59)
            }
        )
        RepeatingPeriod.Yearly -> getLastDateRanges(
            lastDatesRange = stepsRange,
            bottomDateBorderTransformation = { today, i ->
                today.minusYears(i).withMonth(1).withDayOfMonth(1).withHour(0).withMinute(0)
            },
            topDateBorderTransformation = { today, i ->
                today.minusYears(i).withMonth(12).withDayOfMonth(31).withHour(23).withMinute(59)
            }
        )
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


private fun getLastDateRanges(
    lastDatesRange: LongRange,
    bottomDateBorderTransformation: (LocalDateTime, Long) -> LocalDateTime,
    topDateBorderTransformation: (LocalDateTime, Long) -> LocalDateTime
): List<LongDateRange> {
    val today = LocalDateTime.now()
    val list = mutableListOf<LongDateRange>()

    for (i in lastDatesRange.from..lastDatesRange.to) {
        val longDateRange = LongDateRange(
            from = bottomDateBorderTransformation(today, i).asTimestamp(),
            to = topDateBorderTransformation(today, i).asTimestamp()
        )
        list.add(longDateRange)
    }

    return list
}


fun RepeatingPeriod.getColumnNameForColumnChart(
    dateRange: LongDateRange,
    context: Context
): String {
    return when (this) {
        RepeatingPeriod.Daily -> YearMonthDay.fromLong(dateRange.from).getDayWithMonthValueAsString()
        RepeatingPeriod.Weekly -> dateRange.getDayWithMonthValueRangeAsString()
        RepeatingPeriod.Monthly -> dateRange.from.extractMonth().getMonthShortNameRes()
            ?.let { context.getString(it) }
            ?: ""
        RepeatingPeriod.Yearly -> dateRange.from.extractYear().toString()
    }
}


fun Long.extractYear(): Int {
    return (this / 100000000).toInt()
}

fun Long.extractMonth(): Int {
    return (this / 1000000 - this.extractYear() * 100).toInt()
}


fun getNewDateByRecordLongDate(recordDateLong: Long): DateTimeState {
    val calendar = Calendar.getInstance()
    val dateSeparated = YearMonthDayHourMinute.fromLong(recordDateLong)

    calendar.set(
        dateSeparated.year,
        dateSeparated.month - 1,
        dateSeparated.day,
        dateSeparated.hour,
        dateSeparated.minute
    )

    return DateTimeState(
        calendar = calendar,
        dateLong = recordDateLong,
        dateFormatted = calendar.getFormattedDateWithTime()
    )
}


fun convertCalendarMillisToLongWithoutSpecificTime(calendarMillis: Long): Long {
    val calendar = Calendar.getInstance().apply { timeInMillis = calendarMillis }
    return calendar.year.toLong() * 100000000 +
            (calendar.month + 1) * 1000000 +
            calendar.day * 10000
}


fun formatDateRangeForCustomDateRangeField(
    fromPastMillis: Long?,
    toFutureMillis: Long?
): String {
    if (fromPastMillis == null || toFutureMillis == null) {
        return "??? - ???"
    }

    val fromPastCalendar = Calendar.getInstance().apply { timeInMillis = fromPastMillis }
    val toFutureCalendar = Calendar.getInstance().apply { timeInMillis = toFutureMillis }

    val fromLocalDate = LocalDate.now()
        .withYear(fromPastCalendar.year)
        .withMonth(fromPastCalendar.month + 1)
        .withDayOfMonth(fromPastCalendar.day)
    val toLocalDate = LocalDate.now()
        .withYear(toFutureCalendar.year)
        .withMonth(toFutureCalendar.month + 1)
        .withDayOfMonth(toFutureCalendar.day)

    return fromLocalDate.format(DateTimeFormatter.ofPattern("dd.MM.uuuu")) + " - " +
            toLocalDate.format(DateTimeFormatter.ofPattern("dd.MM.uuuu"))
}


fun convertDateLongToDayMonthYear(
    date: Long,
    context: Context,
    includeYear: Boolean = true
): String {
    val dateSeparated = YearMonthDay.fromLong(date)
    val monthString = dateSeparated.month.getMonthShortNameRes()?.let { context.getString(it) } ?: ""

    return if (includeYear) {
        "${dateSeparated.day} $monthString ${dateSeparated.year}"
    } else {
        "${dateSeparated.day} $monthString"
    }
}


fun getFormattedDateFromAndToByFormatDayMonthYear(
    fromPast: Long,
    toFuture: Long,
    context: Context
): String {
    return context.getString(R.string.from_capital) + " " +
            convertDateLongToDayMonthYear(fromPast, context) + "\n" +
            context.getString(R.string.to) + " " +
            convertDateLongToDayMonthYear(toFuture, context)
}


fun LongDateRange.toStringDateRange(period: RepeatingPeriod, context: Context): StringDateRange {
    return StringDateRange(
        from = YearMonthDayHourMinute.fromLong(from).formatByRepeatingPeriod(period, context),
        to = YearMonthDayHourMinute.fromLong(to).formatByRepeatingPeriod(period, context)
    )
}

fun YearMonthDayHourMinute.formatByRepeatingPeriod(
    repeatingPeriod: RepeatingPeriod,
    context: Context
): String {
    return when (repeatingPeriod) {
        RepeatingPeriod.Daily -> "%2d:%2d".format(locale = Locale.US, hour, minute)
        RepeatingPeriod.Weekly, RepeatingPeriod.Monthly -> "$day " + (month.getMonthShortNameRes()
            ?.let { context.getString(it) } ?: "")
        RepeatingPeriod.Yearly -> (month.getMonthShortNameRes()
            ?.let { context.getString(it) } ?: "") + " $year"
    }
}


fun LongDateRange.asTimeInMillisRange(): TimeInMillisRange {
    return TimeInMillisRange(
        from = YearMonthDayHourMinute.fromLong(from).asTimeInMillis(),
        to = YearMonthDayHourMinute.fromLong(to).asTimeInMillis()
    )
}


fun LongDateRange.getCurrentTimeAsGraphPercentageInThisRange(): Float {
    return this.asTimeInMillisRange().getCurrentDateAsGraphPercentageInThisRange()
}


fun Int.getGreetingsWidgetTitleRes(): Int {
    return when (this) {
        in 6..11 -> R.string.greetings_title_morning
        in 12..17 -> R.string.greetings_title_afternoon
        in 18..22 -> R.string.greetings_title_evening
        else -> R.string.greetings_title_night
    }
}
