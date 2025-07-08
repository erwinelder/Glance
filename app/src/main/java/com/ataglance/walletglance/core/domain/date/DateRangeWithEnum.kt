package com.ataglance.walletglance.core.domain.date

import com.ataglance.walletglance.core.presentation.model.ResourceManager
import com.ataglance.walletglance.core.utils.timestampToYear
import com.ataglance.walletglance.core.utils.toTimestampRange
import com.ataglance.walletglance.core.utils.toLocalDateRangeByBasicValues

data class DateRangeWithEnum(
    val enum: DateRangeEnum,
    val dateRange: TimestampRange
) {

    companion object {

        fun fromEnum(enum: DateRangeEnum, currentRange: TimestampRange? = null): DateRangeWithEnum {
            val localDateRange = enum.toLocalDateRangeByBasicValues(timestampRange = currentRange)
            return DateRangeWithEnum(enum = enum, dateRange = localDateRange.toTimestampRange())
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
            DateRangeEnum.Custom -> dateRange.formatAsDayMonthNameYear(resourceManager)
        }
    }

    private fun getFormattedMonth(resourceManager: ResourceManager): String {
        val monthName = enum.getMonthFullNameRes()?.let(resourceManager::getString) ?: ""
        return "$monthName ${dateRange.from.timestampToYear()}"
    }

}