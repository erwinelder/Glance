package com.ataglance.walletglance.core.domain.date

import com.ataglance.walletglance.core.presentation.model.ResourceManager
import com.ataglance.walletglance.core.utils.extractYear
import com.ataglance.walletglance.core.utils.getLocalDateRangeByMonth
import com.ataglance.walletglance.core.utils.getMonthFullNameRes

data class DateRangeWithEnum(
    val enum: DateRangeEnum,
    val dateRange: LongDateRange
) {

    companion object {

        fun fromEnum(enum: DateRangeEnum, currentRange: LongDateRange? = null): DateRangeWithEnum {
            val dateRange = when (enum) {
                DateRangeEnum.ThisWeek -> LocalDateRange.asThisWeek()
                DateRangeEnum.SevenDays -> LocalDateRange.asSevenDays()
                DateRangeEnum.ThisMonth -> LocalDateRange.asThisMonth()
                DateRangeEnum.LastMonth -> LocalDateRange.asLastMonth()
                DateRangeEnum.ThisYear -> LocalDateRange.asThisYear()
                DateRangeEnum.LastYear -> LocalDateRange.asLastYear()
                else -> enum.getLocalDateRangeByMonth(dateRange = currentRange)
            }

            return DateRangeWithEnum(enum = enum, dateRange = dateRange.toLongDateRange())
        }

    }


    fun getEnumStringRepr(resourceManager: ResourceManager): String {
        return when(enum) {
            DateRangeEnum.ThisMonth -> resourceManager.getString(DateRangeAssets.ThisMonth.nameRes)
            DateRangeEnum.LastMonth -> resourceManager.getString(DateRangeAssets.LastMonth.nameRes)
            DateRangeEnum.ThisWeek -> resourceManager.getString(DateRangeAssets.ThisWeek.nameRes)
            DateRangeEnum.SevenDays -> resourceManager.getString(DateRangeAssets.SevenDays.nameRes)
            DateRangeEnum.ThisYear -> resourceManager.getString(DateRangeAssets.ThisYear.nameRes)
            DateRangeEnum.LastYear -> resourceManager.getString(DateRangeAssets.LastYear.nameRes)
            DateRangeEnum.January, DateRangeEnum.February, DateRangeEnum.March,
            DateRangeEnum.April, DateRangeEnum.May, DateRangeEnum.June,
            DateRangeEnum.July, DateRangeEnum.August, DateRangeEnum.September,
            DateRangeEnum.October, DateRangeEnum.November, DateRangeEnum.December ->
                getFormattedMonth(resourceManager)
            DateRangeEnum.Custom -> dateRange.formatAsDayMonthYear(resourceManager)
        }
    }

    private fun getFormattedMonth(resourceManager: ResourceManager): String {
        val monthName = enum.getMonthFullNameRes()?.let { resourceManager.getString(it) } ?: ""
        return "$monthName ${dateRange.from.extractYear()}"
    }

}