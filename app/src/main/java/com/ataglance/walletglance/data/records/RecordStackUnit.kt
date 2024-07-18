package com.ataglance.walletglance.data.records

import com.ataglance.walletglance.data.categories.CategoryWithSubcategory

data class RecordStackUnit(
    val id: Int = 0,
    val amount: Double,
    val quantity: Int?,
    val categoryWithSubcategory: CategoryWithSubcategory?,
    val note: String?
)