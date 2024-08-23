package com.ataglance.walletglance.domain.date

import java.time.LocalDate

data class YearMonthDay(
    val year: Int,
    val month: Int,
    val day: Int
) {

    fun concatenate(): Long {
        return year.toLong() * 100000000 + month * 1000000 + day * 10000
    }

    fun getDayWithMonthValueAsString(): String {
        return "$day.$month"
    }

    fun addYears(yearsToAdd: Int): YearMonthDay {
        val localDate = LocalDate.of(year, month, day)
        localDate.plusYears(yearsToAdd.toLong())
        return YearMonthDay(localDate.year, localDate.monthValue, localDate.dayOfMonth)
    }

    fun subtractYears(yearsToSubtract: Int): YearMonthDay {
        val localDate = LocalDate.of(year, month, day)
        localDate.minusYears(yearsToSubtract.toLong())
        return YearMonthDay(localDate.year, localDate.monthValue, localDate.dayOfMonth)
    }

    fun addMonths(monthsToAdd: Int): YearMonthDay {
        val localDate = LocalDate.of(year, month, day)
        localDate.plusMonths(monthsToAdd.toLong())
        return YearMonthDay(localDate.year, localDate.monthValue, localDate.dayOfMonth)
    }

    fun subtractMonths(monthsToSubtract: Int): YearMonthDay {
        val localDate = LocalDate.of(year, month, day)
        localDate.minusMonths(monthsToSubtract.toLong())
        return YearMonthDay(localDate.year, localDate.monthValue, localDate.dayOfMonth)
    }

    fun addWeeks(weeksToAdd: Int): YearMonthDay {
        val localDate = LocalDate.of(year, month, day)
        localDate.plusWeeks(weeksToAdd.toLong())
        return YearMonthDay(localDate.year, localDate.monthValue, localDate.dayOfMonth)
    }

    fun subtractWeeks(weeksToSubtract: Int): YearMonthDay {
        val localDate = LocalDate.of(year, month, day)
        localDate.minusWeeks(weeksToSubtract.toLong())
        return YearMonthDay(localDate.year, localDate.monthValue, localDate.dayOfMonth)
    }

    fun addDays(daysToAdd: Int): YearMonthDay {
        val localDate = LocalDate.of(year, month, day)
        localDate.plusDays(daysToAdd.toLong())
        return YearMonthDay(localDate.year, localDate.monthValue, localDate.dayOfMonth)
    }

    fun subtractDays(daysToSubtract: Int): YearMonthDay {
        val localDate = LocalDate.of(year, month, day)
        localDate.minusDays(daysToSubtract.toLong())
        return YearMonthDay(localDate.year, localDate.monthValue, localDate.dayOfMonth)
    }

}
