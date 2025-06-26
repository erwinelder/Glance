package com.ataglance.walletglance.core.domain.date

import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.presentation.model.ResourceManager
import com.ataglance.walletglance.core.presentation.utils.formatAsDayMonth
import com.ataglance.walletglance.core.presentation.utils.getMonthShortNameRes
import com.ataglance.walletglance.core.utils.fromTimestamp
import com.ataglance.walletglance.core.utils.timestampToMonth
import com.ataglance.walletglance.core.utils.timestampToYear
import kotlinx.datetime.LocalDateTime

enum class RepeatingPeriod {
    Daily, Weekly, Monthly, Yearly;

    companion object {

        fun asList(): List<RepeatingPeriod> {
            return listOf(Daily, Weekly, Monthly, Yearly)
        }

    }

}


fun RepeatingPeriod.asStringRes(): Int {
    return when (this) {
        RepeatingPeriod.Daily -> R.string.daily
        RepeatingPeriod.Weekly -> R.string.weekly
        RepeatingPeriod.Monthly -> R.string.monthly
        RepeatingPeriod.Yearly -> R.string.yearly
    }
}

fun RepeatingPeriod.getSpendingInRecentStringRes(): Int {
    return when (this) {
        RepeatingPeriod.Daily -> R.string.spending_in_recent_days_with_currency
        RepeatingPeriod.Weekly -> R.string.spending_in_recent_weeks_with_currency
        RepeatingPeriod.Monthly -> R.string.spending_in_recent_months_with_currency
        RepeatingPeriod.Yearly -> R.string.spending_in_recent_years_with_currency
    }
}

fun RepeatingPeriod.getDefaultRangesCount(): Int {
    return when (this) {
        RepeatingPeriod.Daily -> 7
        RepeatingPeriod.Weekly -> 4
        RepeatingPeriod.Monthly -> 6
        RepeatingPeriod.Yearly -> 3
    }
}

fun RepeatingPeriod.getColumnNameForColumnChart(
    dateRange: TimestampRange,
    resourceManager: ResourceManager
): String {
    return when (this) {
        RepeatingPeriod.Daily -> LocalDateTime.fromTimestamp(dateRange.from).formatAsDayMonth()
        RepeatingPeriod.Weekly -> dateRange.formatAsDayMonth()
        RepeatingPeriod.Monthly -> resourceManager.getString(
            id = dateRange.from.timestampToMonth().getMonthShortNameRes()
        )
        RepeatingPeriod.Yearly -> dateRange.from.timestampToYear().toString()
    }
}
