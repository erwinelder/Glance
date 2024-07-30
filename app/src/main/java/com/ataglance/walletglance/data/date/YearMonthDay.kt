package com.ataglance.walletglance.data.date

import java.time.LocalDate

data class YearMonthDay(
    val year: Int,
    val month: Int,
    val day: Int
) {

    fun concatenate(): Long {
        return year.toLong() * 100000000 + month * 1000000 + day * 10000
    }

    fun addYears(yearsToAdd: Int): YearMonthDay {
        val localDate = LocalDate.of(year, month, day)
        localDate.plusYears(yearsToAdd.toLong())
        return YearMonthDay(localDate.year, localDate.monthValue, localDate.dayOfMonth)
    }

    fun addMonths(monthsToAdd: Int): YearMonthDay {
        val localDate = LocalDate.of(year, month, day)
        localDate.plusMonths(monthsToAdd.toLong())
        return YearMonthDay(localDate.year, localDate.monthValue, localDate.dayOfMonth)
    }

    fun addDays(daysToAdd: Int): YearMonthDay {
        val localDate = LocalDate.of(year, month, day)
        localDate.plusDays(daysToAdd.toLong())
        return YearMonthDay(localDate.year, localDate.monthValue, localDate.dayOfMonth)
    }

}
