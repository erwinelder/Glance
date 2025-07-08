package com.ataglance.walletglance.record.data.model

data class RecordItemDataModel(
    val id: Long,
    val recordId: Long,
    val totalAmount: Double,
    val quantity: Int?,
    val categoryId: Int,
    val subcategoryId: Int?,
    val note: String?
)
