package com.ataglance.walletglance.core.domain.date

import java.util.Calendar

data class TimeInMillisRange(
    val from: Long,
    val to: Long
) {

    fun getCurrentDateAsGraphPercentageInThisRange(): Float {
        val currentTime = Calendar.getInstance().timeInMillis
        return ((currentTime - from).toDouble() / (to - from).toDouble()).toFloat()
    }

}
