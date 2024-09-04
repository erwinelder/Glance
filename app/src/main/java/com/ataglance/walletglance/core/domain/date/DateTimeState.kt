package com.ataglance.walletglance.core.domain.date

import com.ataglance.walletglance.core.utils.day
import com.ataglance.walletglance.core.utils.getFormattedDateWithTime
import com.ataglance.walletglance.core.utils.hour
import com.ataglance.walletglance.core.utils.minute
import com.ataglance.walletglance.core.utils.month
import com.ataglance.walletglance.core.utils.toLongWithTime
import com.ataglance.walletglance.core.utils.year
import java.util.Calendar

data class DateTimeState(
    val calendar: Calendar = Calendar.getInstance(),
    val dateLong: Long = Calendar.getInstance().toLongWithTime(),
    val dateFormatted: String = Calendar.getInstance().getFormattedDateWithTime()
) {

    fun getHourAndMinute(): Pair<Int, Int> = calendar.hour to calendar.minute

    fun getNewDate(selectedDateMillis: Long): DateTimeState {
        val calendar = Calendar.getInstance().apply { timeInMillis = selectedDateMillis }
        calendar.set(
            calendar.year, calendar.month, calendar.day,
            this.calendar.hour, this.calendar.minute
        )
        return DateTimeState(
            calendar = calendar,
            dateLong = calendar.toLongWithTime(),
            dateFormatted = calendar.getFormattedDateWithTime()
        )
    }

    fun getNewTime(hour: Int, minute: Int): DateTimeState {
        val calendar = this.calendar
        calendar.set(calendar.year, calendar.month, calendar.day, hour, minute)
        return DateTimeState(
            calendar = calendar,
            dateLong = calendar.toLongWithTime(),
            dateFormatted = calendar.getFormattedDateWithTime()
        )
    }

}