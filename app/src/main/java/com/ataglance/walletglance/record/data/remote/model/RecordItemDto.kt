package com.ataglance.walletglance.record.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class RecordItemDto(
    val id: Long,
    val recordId: Long,
    val totalAmount: Double,
    val quantity: Int?,
    val categoryId: Int,
    val subcategoryId: Int?,
    val note: String?
)
