package com.ataglance.walletglance.recordCreation.domain.record

import com.ataglance.walletglance.category.domain.model.CategoryWithSub

data class CreatedRecordItem(
    val categoryWithSub: CategoryWithSub,
    val note: String?,
    val totalAmount: Double,
    val quantity: Int?
)
