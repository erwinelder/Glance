package com.ataglance.walletglance.record.domain.model

import androidx.compose.runtime.Stable
import com.ataglance.walletglance.category.domain.model.CategoryWithSub

@Stable
data class RecordStackItem(
    val id: Int = 0,
    val amount: Double,
    val quantity: Int?,
    val categoryWithSub: CategoryWithSub?,
    val note: String?,
    val includeInBudgets: Boolean
) {

    fun matchesCategory(categoryWithSub: CategoryWithSub?): Boolean {
        return categoryWithSub?.let { this.categoryWithSub?.match(it) } ?: false
    }

}