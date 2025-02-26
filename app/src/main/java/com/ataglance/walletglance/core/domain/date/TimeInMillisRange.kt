package com.ataglance.walletglance.core.domain.date

import com.ataglance.walletglance.core.utils.getCurrentTimeInMillis
import com.ataglance.walletglance.core.utils.timeInMillisToLocalDate
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.char

data class TimeInMillisRange(
    val from: Long,
    val to: Long
) {

    companion object {

        fun from(fromTimeInMillis: Long?, toTimeInMillis: Long?): TimeInMillisRange? {
            if (fromTimeInMillis == null || toTimeInMillis == null) {
                return null
            }
            return TimeInMillisRange(from = fromTimeInMillis, to = toTimeInMillis)
        }

    }


    fun getCurrentDateAsGraphPercentageInThisRange(): Float {
        return ((getCurrentTimeInMillis() - from).toDouble() / (to - from).toDouble()).toFloat()
    }

    fun formatDateRangeByTimeInMillis(): String {
        val format = LocalDate.Format {
            dayOfMonth(); char('.'); monthNumber(); char('.'); year()
        }

        val fromDate = timeInMillisToLocalDate(from).format(format)
        val toDate = timeInMillisToLocalDate(to).format(format)

        return "$fromDate - $toDate"
    }

}
