package com.ataglance.walletglance.core.domain.date

import com.ataglance.walletglance.core.utils.getCurrentLocalDate
import com.ataglance.walletglance.core.utils.minusDays
import com.ataglance.walletglance.core.utils.minusMonths
import com.ataglance.walletglance.core.utils.minusWeeks
import com.ataglance.walletglance.core.utils.minusYears
import com.ataglance.walletglance.core.utils.plusDays
import kotlinx.datetime.LocalDate

data class LocalDateRange(
    val from: LocalDate,
    val to: LocalDate
) {

    companion object {

        fun asSevenDays(): LocalDateRange {
            val today = getCurrentLocalDate()
            val sixDaysBefore = today.minusDays(6)

            return LocalDateRange(sixDaysBefore, today)
        }


        fun asToday(): LocalDateRange = asDayBefore(0)

        fun asDayBefore(daysToSubtract: Int): LocalDateRange {
            val today = getCurrentLocalDate()
            val day = today.minusDays(daysToSubtract)

            return LocalDateRange(day, day)
        }


        fun asThisWeek(): LocalDateRange = asWeekBefore(0)

        fun asWeekBefore(weeksToSubtract: Int): LocalDateRange {
            val today = getCurrentLocalDate()
            val monday = today.minusDays(today.dayOfWeek.ordinal).minusWeeks(weeksToSubtract)
            val sunday = monday.plusDays(6)

            return LocalDateRange(monday, sunday)
        }


        fun asThisMonth(): LocalDateRange = asMonthBefore(0)

        fun asLastMonth(): LocalDateRange = asMonthBefore(1)

        fun asMonthBefore(monthsToSubtract: Int): LocalDateRange {
            val today = getCurrentLocalDate()
            val firstDay = LocalDate(today.year, today.monthNumber, 1).minusMonths(monthsToSubtract)
            val lastDay = LocalDate(today.year, today.monthNumber, 1).minusMonths(monthsToSubtract - 1)
                .minusDays(1)

            return LocalDateRange(firstDay, lastDay)
        }


        fun asThisYear(): LocalDateRange = asYearBefore(0)

        fun asLastYear(): LocalDateRange = asYearBefore(1)

        fun asYearBefore(yearsToSubtract: Int): LocalDateRange {
            val today = getCurrentLocalDate()
            val firstDay = LocalDate(today.year, 1, 1).minusYears(yearsToSubtract)
            val lastDay = LocalDate(today.year, 12, 31).minusYears(yearsToSubtract)

            return LocalDateRange(firstDay, lastDay)
        }

    }

}