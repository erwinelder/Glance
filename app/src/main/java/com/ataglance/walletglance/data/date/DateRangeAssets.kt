package com.ataglance.walletglance.data.date

import androidx.annotation.StringRes
import com.ataglance.walletglance.R

sealed class DateRangeAssets(val enum: DateRangeEnum, @StringRes val nameRes: Int) {
    data object ThisMonth : DateRangeAssets(DateRangeEnum.ThisMonth, R.string.this_month)
    data object LastMonth : DateRangeAssets(DateRangeEnum.LastMonth, R.string.last_month)
    data object ThisWeek : DateRangeAssets(DateRangeEnum.ThisWeek, R.string.this_week)
    data object SevenDays : DateRangeAssets(DateRangeEnum.SevenDays, R.string.seven_days)
    data object ThisYear : DateRangeAssets(DateRangeEnum.ThisYear, R.string.this_year)
    data object LastYear : DateRangeAssets(DateRangeEnum.LastYear, R.string.last_year)
    data object January : DateRangeAssets(DateRangeEnum.January, R.string.january_short)
    data object February : DateRangeAssets(DateRangeEnum.February, R.string.february_short)
    data object March : DateRangeAssets(DateRangeEnum.March, R.string.march_short)
    data object April : DateRangeAssets(DateRangeEnum.April, R.string.april_short)
    data object May : DateRangeAssets(DateRangeEnum.May, R.string.may_short)
    data object June : DateRangeAssets(DateRangeEnum.June, R.string.june_short)
    data object July : DateRangeAssets(DateRangeEnum.July, R.string.july_short)
    data object August : DateRangeAssets(DateRangeEnum.August, R.string.august_short)
    data object September : DateRangeAssets(DateRangeEnum.September, R.string.september_short)
    data object October : DateRangeAssets(DateRangeEnum.October, R.string.october_short)
    data object November : DateRangeAssets(DateRangeEnum.November, R.string.november_short)
    data object December : DateRangeAssets(DateRangeEnum.December, R.string.december_short)
}