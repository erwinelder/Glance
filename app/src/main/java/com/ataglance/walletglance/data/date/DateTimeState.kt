package com.ataglance.walletglance.data.date

import com.ataglance.walletglance.domain.utils.day
import com.ataglance.walletglance.domain.utils.getFormattedDateWithTime
import com.ataglance.walletglance.domain.utils.hour
import com.ataglance.walletglance.domain.utils.minute
import com.ataglance.walletglance.domain.utils.month
import com.ataglance.walletglance.domain.utils.toLongWithTime
import com.ataglance.walletglance.domain.utils.year
import java.util.Calendar

data class DateTimeState(
    val calendar: Calendar = Calendar.getInstance(),
    val dateLong: Long = Calendar.getInstance().toLongWithTime(),
    val dateFormatted: String = Calendar.getInstance().getFormattedDateWithTime()
) {

    fun getNewDate(selectedDateMillis: Long): DateTimeState {
        val calendar = Calendar.getInstance().apply { timeInMillis = selectedDateMillis }
        calendar.set(
            calendar.year(), calendar.month(), calendar.day(),
            this.calendar.hour(), this.calendar.minute()
        )
        return DateTimeState(
            calendar = calendar,
            dateLong = calendar.toLongWithTime(),
            dateFormatted = calendar.getFormattedDateWithTime()
        )
    }

    fun getNewTime(hour: Int, minute: Int): DateTimeState {
        val calendar = this.calendar
        calendar.set(calendar.year(), calendar.month(), calendar.day(), hour, minute)
        return DateTimeState(
            calendar = calendar,
            dateLong = calendar.toLongWithTime(),
            dateFormatted = calendar.getFormattedDateWithTime()
        )
    }

}