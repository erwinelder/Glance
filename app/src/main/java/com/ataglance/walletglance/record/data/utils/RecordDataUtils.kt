package com.ataglance.walletglance.record.data.utils

import com.ataglance.walletglance.record.data.local.model.RecordEntity
import com.ataglance.walletglance.record.data.local.model.RecordEntityWithItems
import com.ataglance.walletglance.record.data.local.model.RecordItemEntity


fun List<RecordEntity>.zipWithItems(items: List<RecordItemEntity>): List<RecordEntityWithItems> {
    val items = items.groupBy { it.recordId }

    return mapNotNull { record ->
        RecordEntityWithItems(
            record = record,
            items = items[record.id] ?: return@mapNotNull null
        )
    }
}
