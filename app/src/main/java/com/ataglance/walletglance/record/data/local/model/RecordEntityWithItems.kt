package com.ataglance.walletglance.record.data.local.model

data class RecordEntityWithItems(
    val record: RecordEntity,
    val items: List<RecordItemEntity>
) {

    val recordId: Long
        get() = record.id

    val deleted: Boolean
        get() = record.deleted


    fun replaceRecordId(id: Long): RecordEntityWithItems {
        return copy(
            record = record.copy(id = id),
            items = items.map { it.copy(recordId = id) }
        )
    }

}
