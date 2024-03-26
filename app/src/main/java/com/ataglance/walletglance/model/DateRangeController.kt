package com.ataglance.walletglance.model

import android.content.Context
import androidx.annotation.StringRes
import com.ataglance.walletglance.R
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

enum class DateRangeEnum {
    Custom, ThisWeek, SevenDays, ThisMonth, LastMonth, ThisYear, LastYear,
    January, February, March, April, May, June, July, August, September, October, November, December
}

sealed class DateRange(val enum: DateRangeEnum, @StringRes val nameRes: Int) {
    data object ThisMonth : DateRange(DateRangeEnum.ThisMonth, R.string.this_month)
    data object LastMonth : DateRange(DateRangeEnum.LastMonth, R.string.last_month)
    data object ThisWeek : DateRange(DateRangeEnum.ThisWeek, R.string.this_week)
    data object SevenDays : DateRange(DateRangeEnum.SevenDays, R.string.seven_days)
    data object ThisYear : DateRange(DateRangeEnum.ThisYear, R.string.this_year)
    data object LastYear : DateRange(DateRangeEnum.LastYear, R.string.last_year)
    data object January : DateRange(DateRangeEnum.January, R.string.january_short)
    data object February : DateRange(DateRangeEnum.February, R.string.february_short)
    data object March : DateRange(DateRangeEnum.March, R.string.march_short)
    data object April : DateRange(DateRangeEnum.April, R.string.april_short)
    data object May : DateRange(DateRangeEnum.May, R.string.may_short)
    data object June : DateRange(DateRangeEnum.June, R.string.june_short)
    data object July : DateRange(DateRangeEnum.July, R.string.july_short)
    data object August : DateRange(DateRangeEnum.August, R.string.august_short)
    data object September : DateRange(DateRangeEnum.September, R.string.september_short)
    data object October : DateRange(DateRangeEnum.October, R.string.october_short)
    data object November : DateRange(DateRangeEnum.November, R.string.november_short)
    data object December : DateRange(DateRangeEnum.December, R.string.december_short)
}

class DateRangeController {

    private fun Calendar.year(): Int {
        return this.get(Calendar.YEAR)
    }

    private fun Calendar.month(): Int {
        return this.get(Calendar.MONTH)
    }

    private fun Calendar.day(): Int {
        return this.get(Calendar.DAY_OF_MONTH)
    }

    private fun Calendar.hour(): Int {
        return this.get(Calendar.HOUR_OF_DAY)
    }

    private fun Calendar.minute(): Int {
        return this.get(Calendar.MINUTE)
    }


    fun getTodayDateLong(): Long {
        return convertDateToLongWithoutTime(LocalDate.now())
    }

    private fun convertDateToLongWithoutTime(date: LocalDate): Long {
        return date.year.toLong() * 100000000 +
                date.monthValue * 1000000 +
                date.dayOfMonth * 10000
    }

    fun convertCalendarToLongWithTime(calendar: Calendar): Long {
        return calendar.year().toLong() * 100000000 +
            (calendar.month() + 1) * 1000000 +
            calendar.day() * 10000 +
            calendar.hour() * 100 +
            calendar.minute()
    }

    fun convertCalendarMillisToLongWithoutSpecificTime(calendarMillis: Long): Long {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = calendarMillis
        }
        return calendar.year().toLong() * 100000000 +
            (calendar.month() + 1) * 1000000 +
            calendar.day() * 10000
    }

    private fun convertLocalDateToCalendarLong(localDate: LocalDate): Long {
        val calendar = Calendar.getInstance()
        calendar.set(localDate.year, localDate.monthValue - 1, localDate.dayOfMonth)
        return calendar.timeInMillis
    }

    fun formatCalendarDateWithTime(calendar: Calendar): String {
        val localDateTime = LocalDateTime.now()
            .withYear(calendar.year())
            .withMonth(calendar.month() + 1)
            .withDayOfMonth(calendar.day())
            .withHour(calendar.hour())
            .withMinute(calendar.minute())
        return localDateTime.format(DateTimeFormatter.ofPattern("dd.MM.uuuu, HH:mm"))
    }

    fun formatDateRangeForCustomDateRangeField(
        fromPastMillis: Long?, toFutureMillis: Long?
    ): String {
        if (fromPastMillis == null || toFutureMillis == null) {
            return "??? - ???"
        }

        val fromPastCalendar = Calendar.getInstance().apply {
            timeInMillis = fromPastMillis
        }
        val toFutureCalendar = Calendar.getInstance().apply {
            timeInMillis = toFutureMillis
        }
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

    private fun convertTwoDatesToDateRangeState(
        enum: DateRangeEnum, localDateRange: LocalDateRange
    ): DateRangeState {
        return DateRangeState(
            enum = enum,
            fromPast = convertDateToLongWithoutTime(localDateRange.startPast),
            toFuture = convertDateToLongWithoutTime(localDateRange.endFuture) + 2359
        )
    }


    private fun getMonthValue(dateRangeEnum: DateRangeEnum): Int? {
        return when (dateRangeEnum) {
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

    private fun getSpecifiedMonthLocalDateRange(
        month: DateRangeEnum, currentDateRangeState: DateRangeState?
    ): LocalDateRange {
        val monthValue = getMonthValue(month)

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


    fun getDateRangeStateByEnum(
        dateRangeEnum: DateRangeEnum, currentDateRangeState: DateRangeState? = null
    ): DateRangeState {
        return when (dateRangeEnum) {
            DateRangeEnum.ThisWeek -> convertTwoDatesToDateRangeState(
                DateRangeEnum.ThisWeek, getThisWeekLocalDateRange()
            )
            DateRangeEnum.SevenDays -> convertTwoDatesToDateRangeState(
                DateRangeEnum.SevenDays, getSevenDaysLocalDateRange()
            )
            DateRangeEnum.ThisMonth -> convertTwoDatesToDateRangeState(
                DateRangeEnum.ThisMonth, getThisMonthLocalDateRange()
            )
            DateRangeEnum.LastMonth -> convertTwoDatesToDateRangeState(
                DateRangeEnum.LastMonth, getLastMonthLocalDateRange()
            )
            DateRangeEnum.ThisYear -> convertTwoDatesToDateRangeState(
                DateRangeEnum.ThisYear, getThisYearLocalDateRange()
            )
            DateRangeEnum.LastYear -> convertTwoDatesToDateRangeState(
                DateRangeEnum.LastYear, getLastYearLocalDateRange()
            )
            else -> convertTwoDatesToDateRangeState(
                dateRangeEnum,
                getSpecifiedMonthLocalDateRange(dateRangeEnum, currentDateRangeState)
            )
        }
    }

    fun getCalendarStartLongByEnum(
        dateRangeEnum: DateRangeEnum, currentDateRangeState: DateRangeState? = null
    ): Long {
        return when(dateRangeEnum) {
            DateRangeEnum.ThisMonth -> convertLocalDateToCalendarLong(
                getThisMonthLocalDateRange().startPast
            )
            DateRangeEnum.LastMonth -> convertLocalDateToCalendarLong(
                getLastMonthLocalDateRange().startPast
            )
            DateRangeEnum.ThisWeek -> convertLocalDateToCalendarLong(
                getThisWeekLocalDateRange().startPast
            )
            DateRangeEnum.SevenDays -> convertLocalDateToCalendarLong(
                getSevenDaysLocalDateRange().startPast
            )
            DateRangeEnum.ThisYear -> convertLocalDateToCalendarLong(
                getThisYearLocalDateRange().startPast
            )
            DateRangeEnum.LastYear -> convertLocalDateToCalendarLong(
                getLastYearLocalDateRange().startPast
            )
            else -> convertLocalDateToCalendarLong(
                getSpecifiedMonthLocalDateRange(dateRangeEnum, currentDateRangeState).startPast
            )
        }
    }

    fun getCalendarEndLongByEnum(
        dateRangeEnum: DateRangeEnum, currentDateRangeState: DateRangeState? = null
    ): Long {
        return when(dateRangeEnum) {
            DateRangeEnum.ThisMonth -> convertLocalDateToCalendarLong(
                getThisMonthLocalDateRange().endFuture
            )
            DateRangeEnum.LastMonth -> convertLocalDateToCalendarLong(
                getLastMonthLocalDateRange().endFuture
            )
            DateRangeEnum.ThisWeek -> convertLocalDateToCalendarLong(
                getThisWeekLocalDateRange().endFuture
            )
            DateRangeEnum.SevenDays -> convertLocalDateToCalendarLong(
                getSevenDaysLocalDateRange().endFuture
            )
            DateRangeEnum.ThisYear -> convertLocalDateToCalendarLong(
                getThisYearLocalDateRange().endFuture
            )
            DateRangeEnum.LastYear -> convertLocalDateToCalendarLong(
                getLastYearLocalDateRange().endFuture
            )
            else -> convertLocalDateToCalendarLong(
                getSpecifiedMonthLocalDateRange(dateRangeEnum, currentDateRangeState).endFuture
            )
        }
    }

    fun convertDateLongToDayMonthYear(date: Long, includeYear: Boolean = true): String {
        val year = (date / 100000000).toInt()
        val month = date / 1000000 - year * 100
        val day = date / 10000 - year * 10000 - month * 100

        return if (includeYear) "$day.$month.$year" else "$day.$month"
    }

    fun getFormattedDateFromAndToByFormatDayMonthYear(
        fromPast: Long, toFuture: Long, context: Context
    ): String {
        return context.getString(R.string.from_capital) + " " +
                DateRangeController().convertDateLongToDayMonthYear(fromPast) + "\n" +
                context.getString(R.string.to) + " " +
                DateRangeController().convertDateLongToDayMonthYear(toFuture)
    }

}

data class DateRangeState(
    val enum: DateRangeEnum,
    val fromPast: Long,
    val toFuture: Long
) {
    fun getFormattedMonth(context: Context): String {
        return when (enum) {
            DateRangeEnum.January -> context.getString(R.string.january_full)
            DateRangeEnum.February -> context.getString(R.string.february_full)
            DateRangeEnum.March -> context.getString(R.string.march_full)
            DateRangeEnum.April -> context.getString(R.string.april_full)
            DateRangeEnum.May -> context.getString(R.string.may_full)
            DateRangeEnum.June -> context.getString(R.string.june_full)
            DateRangeEnum.July -> context.getString(R.string.july_full)
            DateRangeEnum.August -> context.getString(R.string.august_full)
            DateRangeEnum.September -> context.getString(R.string.september_full)
            DateRangeEnum.October -> context.getString(R.string.october_full)
            DateRangeEnum.November -> context.getString(R.string.november_full)
            else -> context.getString(R.string.december_full)
        } + " ${fromPast / 100000000}"
    }
}

private data class LocalDateRange(
    val startPast: LocalDate,
    val endFuture: LocalDate
)