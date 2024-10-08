package com.ataglance.walletglance.record.domain

import androidx.compose.runtime.Stable
import com.ataglance.walletglance.category.domain.CategoryWithSubcategory

@Stable
data class RecordStackItem(
    val id: Int = 0,
    val amount: Double,
    val quantity: Int?,
    val categoryWithSubcategory: CategoryWithSubcategory?,
    val note: String?,
    val includeInBudgets: Boolean
) {

    fun matchesCategory(categoryWithSubcategory: CategoryWithSubcategory?): Boolean {
        return categoryWithSubcategory?.let { this.categoryWithSubcategory?.match(it) } ?: false
    }

}