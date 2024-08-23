package com.ataglance.walletglance.domain.records

import com.ataglance.walletglance.domain.categories.CategoryWithSubcategory

data class RecordStackUnit(
    val id: Int = 0,
    val amount: Double,
    val quantity: Int?,
    val categoryWithSubcategory: CategoryWithSubcategory?,
    val note: String?,
    val includeInBudgets: Boolean
)