package com.ataglance.walletglance.data.date

import android.content.Context
import com.ataglance.walletglance.R

data class DateRangeWithEnum(
    val enum: DateRangeEnum,
    val dateRange: LongDateRange
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
        } + " ${dateRange.from / 100000000}"
    }

    fun getRangePair(): Pair<Long, Long> {
        return dateRange.from to dateRange.to
    }

}