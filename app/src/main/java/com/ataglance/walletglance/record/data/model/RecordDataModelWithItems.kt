package com.ataglance.walletglance.record.data.model

data class RecordDataModelWithItems(
    val record: RecordDataModel,
    val items: List<RecordItemDataModel>
) {

    val type: Char
        get() = record.type

    val accountId: Int
        get() = record.accountId

    val totalAmount: Double
        get() = items.sumOf { it.totalAmount }

}
