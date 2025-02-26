package com.ataglance.walletglance.core.domain.date

import com.ataglance.walletglance.core.utils.asTimestamp
import kotlinx.datetime.LocalDateTime

data class LocalDateTimeRange(
    val from: LocalDateTime,
    val to: LocalDateTime
) {

    fun toLongDateRange(): LongDateRange {
        return LongDateRange(from = from.asTimestamp(), to = to.asTimestamp())
    }

}