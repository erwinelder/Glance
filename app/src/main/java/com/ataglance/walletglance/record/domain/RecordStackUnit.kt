package com.ataglance.walletglance.record.domain

import com.ataglance.walletglance.category.domain.CategoryWithSubcategory

data class RecordStackUnit(
    val id: Int = 0,
    val amount: Double,
    val quantity: Int?,
    val categoryWithSubcategory: CategoryWithSubcategory?,
    val note: String?,
    val includeInBudgets: Boolean
)