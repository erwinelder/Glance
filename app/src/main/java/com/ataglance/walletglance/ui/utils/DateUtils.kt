package com.ataglance.walletglance.ui.utils

import android.content.Context
import com.ataglance.walletglance.R
import com.ataglance.walletglance.data.date.DateRangeEnum
import com.ataglance.walletglance.data.date.DateRangeState
import com.ataglance.walletglance.data.date.DateTimeState
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar


private data class LocalDateRange(
    val startPast: LocalDate,
    val endFuture: LocalDate
) {

    fun toDateRangeState(
        enum: DateRangeEnum
    ): DateRangeState {
        return DateRangeState(
            enum = enum,
            fromPast = startPast.toLongWithoutTime(),
            toFuture = endFuture.toLongWithoutTime() + 2359
        )
    }

}


fun Calendar.year(): Int {
    return this.get(Calendar.YEAR)
}


fun Calendar.month(): Int {
    return this.get(Calendar.MONTH)
}


fun Calendar.day(): Int {
    return this.get(Calendar.DAY_OF_MONTH)
}


fun Calendar.hour(): Int {
    return this.get(Calendar.HOUR_OF_DAY)
}


fun Calendar.minute(): Int {
    return this.get(Calendar.MINUTE)
}


fun Calendar.toLongWithTime(): Long {
    return this.year().toLong() * 100000000 +
            (this.month() + 1) * 1000000 +
            this.day() * 10000 +
            this.hour() * 100 +
            this.minute()
}


fun Calendar.getFormattedDateWithTime(): String {
    val localDateTime = LocalDateTime.now()
        .withYear(this.year())
        .withMonth(this.month() + 1)
        .withDayOfMonth(this.day())
        .withHour(this.hour())
        .withMinute(this.minute())
    return localDateTime.format(DateTimeFormatter.ofPattern("dd.MM.uuuu, HH:mm"))
}


fun LocalDate.toLongWithoutTime(): Long {
    return this.year.toLong() * 100000000 +
            this.monthValue * 1000000 +
            this.dayOfMonth * 10000
}


fun LocalDate.toCalendarLong(): Long {
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
    month: DateRangeEnum, currentDateRangeState: DateRangeState?
): LocalDateRange {
    val monthValue = month.getMonthValue()

    if (monthValue == null || currentDateRangeState == null) {
        return getThisMonthLocalDateRange()
    }

    val today = LocalDate.now()
    val firstDay = today.withYear((currentDateRangeState.fromPast / 100000000).toInt())
        .withMonth(monthValue).withDayOfMonth(1)
    val lastDay = today.withYear((currentDateRangeState.toFuture / 100000000).toInt())
        .withMonth(monthValue).plusMonths(1).withDayOfMonth(1).minusDays(1)

    return LocalDateRange(firstDay, lastDay)
}


fun DateRangeEnum.getDateRangeState(
    currentDateRangeState: DateRangeState? = null
): DateRangeState {
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
        else -> getLocalDateRangeBySpecificMonth(this, currentDateRangeState)
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


private fun getMonthStringByMonthValue(value: Int, context: Context): String? {
    return when (value) {
        1 -> context.getString(R.string.january_short)
        2 -> context.getString(R.string.february_short)
        3 -> context.getString(R.string.march_short)
        4 -> context.getString(R.string.april_short)
        5 -> context.getString(R.string.may_short)
        6 -> context.getString(R.string.june_short)
        7 -> context.getString(R.string.july_short)
        8 -> context.getString(R.string.august_short)
        9 -> context.getString(R.string.september_short)
        10 -> context.getString(R.string.october_short)
        11 -> context.getString(R.string.november_short)
        12 -> context.getString(R.string.december_short)
        else -> null
    }
}


fun DateRangeEnum.getCalendarStartLong(currentDateRangeState: DateRangeState? = null): Long {
    return when(this) {
        DateRangeEnum.ThisMonth -> getThisMonthLocalDateRange().startPast.toCalendarLong()
        DateRangeEnum.LastMonth -> getLastMonthLocalDateRange().startPast.toCalendarLong()
        DateRangeEnum.ThisWeek -> getThisWeekLocalDateRange().startPast.toCalendarLong()
        DateRangeEnum.SevenDays -> getSevenDaysLocalDateRange().startPast.toCalendarLong()
        DateRangeEnum.ThisYear -> getThisYearLocalDateRange().startPast.toCalendarLong()
        DateRangeEnum.LastYear -> getLastYearLocalDateRange().startPast.toCalendarLong()
        else -> getLocalDateRangeBySpecificMonth(this, currentDateRangeState)
            .startPast.toCalendarLong()
    }
}


fun DateRangeEnum.getCalendarEndLong(currentDateRangeState: DateRangeState? = null): Long {
    return when(this) {
        DateRangeEnum.ThisMonth -> getThisMonthLocalDateRange().endFuture.toCalendarLong()
        DateRangeEnum.LastMonth -> getLastMonthLocalDateRange().endFuture.toCalendarLong()
        DateRangeEnum.ThisWeek -> getThisWeekLocalDateRange().endFuture.toCalendarLong()
        DateRangeEnum.SevenDays -> getSevenDaysLocalDateRange().endFuture.toCalendarLong()
        DateRangeEnum.ThisYear -> getThisYearLocalDateRange().endFuture.toCalendarLong()
        DateRangeEnum.LastYear -> getLastYearLocalDateRange().endFuture.toCalendarLong()
        else -> getLocalDateRangeBySpecificMonth(this, currentDateRangeState)
            .endFuture.toCalendarLong()
    }
}


fun getTodayDateLong(): Long {
    return LocalDate.now().toLongWithoutTime()
}


fun getNewDateByRecordLongDate(recordDateLong: Long): DateTimeState {
    val calendar = Calendar.getInstance()
    val year = (recordDateLong / 100000000).toInt()
    val month = (recordDateLong / 1000000 - year * 100).toInt()
    val day = (recordDateLong / 10000 - year * 10000 - month * 100).toInt()
    val hour = (recordDateLong / 100 - year * 1000000 - month * 10000 - day * 100).toInt()
    val minute = (recordDateLong - year * 100000000 - month * 1000000 - day * 10000 - hour * 100).toInt()
    calendar.set(year, month - 1, day, hour, minute)

    return DateTimeState(
        calendar = calendar,
        dateLong = recordDateLong,
        dateFormatted = calendar.getFormattedDateWithTime()
    )
}


fun convertCalendarMillisToLongWithoutSpecificTime(calendarMillis: Long): Long {
    val calendar = Calendar.getInstance().apply { timeInMillis = calendarMillis }
    return calendar.year().toLong() * 100000000 +
            (calendar.month() + 1) * 1000000 +
            calendar.day() * 10000
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
        .withYear(fromPastCalendar.year())
        .withMonth(fromPastCalendar.month() + 1)
        .withDayOfMonth(fromPastCalendar.day())
    val toLocalDate = LocalDate.now()
        .withYear(toFutureCalendar.year())
        .withMonth(toFutureCalendar.month() + 1)
        .withDayOfMonth(toFutureCalendar.day())

    return fromLocalDate.format(DateTimeFormatter.ofPattern("dd.MM.uuuu")) + " - " +
            toLocalDate.format(DateTimeFormatter.ofPattern("dd.MM.uuuu"))
}


fun convertDateLongToDayMonthYear(
    date: Long,
    context: Context,
    includeYear: Boolean = true
): String {
    val year = (date / 100000000).toInt()
    val month = (date / 1000000 - year * 100).toInt()
    val monthString = getMonthStringByMonthValue(month, context)
    val day = date / 10000 - year * 10000 - month * 100

    return if (includeYear) "$day $monthString $year" else "$day $monthString"
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
