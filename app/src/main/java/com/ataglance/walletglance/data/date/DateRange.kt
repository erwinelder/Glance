package com.ataglance.walletglance.data.date

import androidx.annotation.StringRes
import com.ataglance.walletglance.R

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