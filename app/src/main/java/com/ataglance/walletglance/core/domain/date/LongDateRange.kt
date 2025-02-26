package com.ataglance.walletglance.core.domain.date

import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.presentation.model.ResourceManager
import com.ataglance.walletglance.core.utils.asTimeInMillisRange
import com.ataglance.walletglance.core.utils.formatDateLongAsDayMonthYear
import com.ataglance.walletglance.core.utils.getCurrentDateLong

data class LongDateRange(
    val from: Long,
    val to: Long
) {

    companion object {

        fun asToday(): LongDateRange {
            return getCurrentDateLong().let { LongDateRange(it, it + 2359) }
        }

    }


    fun equalsTo(other: LongDateRange): Boolean {
        return from == other.from && to == other.to
    }

    fun containsDate(date: Long): Boolean {
        return date in from..to
    }

    fun containsRange(range: LongDateRange): Boolean {
        return from <= range.from && to >= range.to
    }


    fun getCurrentTimeAsGraphPercentageInThisRange(): Float {
        return asTimeInMillisRange().getCurrentDateAsGraphPercentageInThisRange()
    }

    fun getDayWithMonthValueRangeAsString(): String {
        return "${YearMonthDay.fromLong(from).getDayWithMonthValueAsString()} - " +
                YearMonthDay.fromLong(to).getDayWithMonthValueAsString()
    }

    fun formatAsDayMonthYear(resourceManager: ResourceManager): String {
        return resourceManager.getString(R.string.from_capital) + " " +
                from.formatDateLongAsDayMonthYear(resourceManager) + "\n" +
                resourceManager.getString(R.string.to) + " " +
                to.formatDateLongAsDayMonthYear(resourceManager)
    }

}