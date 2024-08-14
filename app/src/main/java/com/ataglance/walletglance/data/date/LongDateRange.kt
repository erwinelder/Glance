package com.ataglance.walletglance.data.date

data class LongDateRange(
    val from: Long,
    val to: Long
) {

    fun containsDate(date: Long): Boolean {
        return date in from..to
    }

    fun containsDateRange(dateRange: LongDateRange): Boolean {
        return from <= dateRange.from && to >= dateRange.to
    }

}
