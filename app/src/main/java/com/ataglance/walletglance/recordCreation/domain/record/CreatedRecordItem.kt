package com.ataglance.walletglance.recordCreation.domain.record

import com.ataglance.walletglance.category.domain.CategoryWithSubcategory

data class CreatedRecordItem(
    val categoryWithSubcategory: CategoryWithSubcategory,
    val note: String?,
    val totalAmount: Double,
    val quantity: Int?
)
