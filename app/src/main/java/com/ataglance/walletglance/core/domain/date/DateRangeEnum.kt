package com.ataglance.walletglance.core.domain.date

import com.ataglance.walletglance.R

enum class DateRangeEnum {
    Custom, ThisWeek, SevenDays, ThisMonth, LastMonth, ThisYear, LastYear,
    January, February, March, April, May, June, July, August, September, October, November, December
}


fun DateRangeEnum.getMonthNumber(): Int? {
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

fun DateRangeEnum.getMonthFullNameRes(): Int? {
    return when (this) {
        DateRangeEnum.January -> R.string.january_full
        DateRangeEnum.February -> R.string.february_full
        DateRangeEnum.March -> R.string.march_full
        DateRangeEnum.April -> R.string.april_full
        DateRangeEnum.May -> R.string.may_full
        DateRangeEnum.June -> R.string.june_full
        DateRangeEnum.July -> R.string.july_full
        DateRangeEnum.August -> R.string.august_full
        DateRangeEnum.September -> R.string.september_full
        DateRangeEnum.October -> R.string.october_full
        DateRangeEnum.November -> R.string.november_full
        DateRangeEnum.December -> R.string.december_full
        else -> null
    }
}