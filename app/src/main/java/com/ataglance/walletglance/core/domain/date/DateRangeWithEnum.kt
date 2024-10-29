package com.ataglance.walletglance.core.domain.date

import android.content.Context
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.data.model.LongDateRange
import com.ataglance.walletglance.core.utils.extractYear
import com.ataglance.walletglance.core.utils.getFormattedDateFromAndToByFormatDayMonthYear

data class DateRangeWithEnum(
    val enum: DateRangeEnum,
    val dateRange: LongDateRange
) {

    fun getEnumStringRepr(context: Context): String {
        return when(enum) {
            DateRangeEnum.ThisMonth -> context.getString(DateRangeAssets.ThisMonth.nameRes)
            DateRangeEnum.LastMonth -> context.getString(DateRangeAssets.LastMonth.nameRes)
            DateRangeEnum.ThisWeek -> context.getString(DateRangeAssets.ThisWeek.nameRes)
            DateRangeEnum.SevenDays -> context.getString(DateRangeAssets.SevenDays.nameRes)
            DateRangeEnum.ThisYear -> context.getString(DateRangeAssets.ThisYear.nameRes)
            DateRangeEnum.LastYear -> context.getString(DateRangeAssets.LastYear.nameRes)
            DateRangeEnum.January, DateRangeEnum.February, DateRangeEnum.March,
            DateRangeEnum.April, DateRangeEnum.May, DateRangeEnum.June,
            DateRangeEnum.July, DateRangeEnum.August, DateRangeEnum.September,
            DateRangeEnum.October, DateRangeEnum.November, DateRangeEnum.December ->
                getFormattedMonth(context)
            DateRangeEnum.Custom -> getFormattedDateFromAndToByFormatDayMonthYear(
                dateRange.from, dateRange.to, context
            )
        }
    }

    private fun getFormattedMonth(context: Context): String {
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
        } + " ${dateRange.from.extractYear()}"
    }

}