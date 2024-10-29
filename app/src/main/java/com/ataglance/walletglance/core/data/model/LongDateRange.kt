package com.ataglance.walletglance.core.data.model

import com.ataglance.walletglance.core.utils.extractYearMonthDay

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

    fun getDayWithMonthValueRangeAsString(): String {
        return "${from.extractYearMonthDay().getDayWithMonthValueAsString()} - " +
                to.extractYearMonthDay().getDayWithMonthValueAsString()
    }

}