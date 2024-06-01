package com.ataglance.walletglance.ui.viewmodels

import java.util.Calendar

class DateTimeController {

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

    fun getNewDate(selectedDateMillis: Long, dateTimeState: DateTimeState): DateTimeState {
        val calendar = Calendar.getInstance().apply { timeInMillis = selectedDateMillis }
        calendar.set(
            calendar.year(), calendar.month(), calendar.day(),
            dateTimeState.calendar.hour(), dateTimeState.calendar.minute()
        )
        return DateTimeState(
            calendar = calendar,
            dateLong = DateRangeController().convertCalendarToLongWithTime(calendar),
            dateFormatted = DateRangeController().formatCalendarDateWithTime(calendar)
        )
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
            dateFormatted = DateRangeController().formatCalendarDateWithTime(calendar)
        )
    }

    fun getNewTime(dateTimeState: DateTimeState, hour: Int, minute: Int): DateTimeState {
        val calendar = dateTimeState.calendar
        calendar.set(calendar.year(), calendar.month(), calendar.day(), hour, minute)
        return DateTimeState(
            calendar = calendar,
            dateLong = DateRangeController().convertCalendarToLongWithTime(calendar),
            dateFormatted = DateRangeController().formatCalendarDateWithTime(calendar)
        )
    }

}

data class DateTimeState(
    val calendar: Calendar = Calendar.getInstance(),
    val dateLong: Long = DateRangeController().convertCalendarToLongWithTime(Calendar.getInstance()),
    val dateFormatted: String = DateRangeController().formatCalendarDateWithTime(Calendar.getInstance())
)
