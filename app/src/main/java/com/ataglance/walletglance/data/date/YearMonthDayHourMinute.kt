package com.ataglance.walletglance.data.date

data class YearMonthDayHourMinute(
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int,
    val minute: Int
) {

    fun concatenate(): Long {
        return year.toLong() * 100000000 + month * 1000000 + day * 10000 + hour * 100 + minute
    }

}
