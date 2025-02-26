package com.ataglance.walletglance.record.presentation.model

import com.ataglance.walletglance.record.domain.model.RecordStack
import com.ataglance.walletglance.record.domain.utils.containsRecordsFromDifferentYears

data class RecentRecordsWidgetUiState(
    val firstRecord: RecordStack? = null,
    val secondRecord: RecordStack? = null,
    val thirdRecord: RecordStack? = null
) {

    fun isEmpty(): Boolean {
        return firstRecord == null && secondRecord == null && thirdRecord == null
    }

    fun asList(): List<RecordStack> {
        return listOfNotNull(firstRecord, secondRecord, thirdRecord)
    }

    fun containsRecordsFromDifferentYears(): Boolean {
        return asList().containsRecordsFromDifferentYears()
    }

}