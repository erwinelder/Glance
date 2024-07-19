package com.ataglance.walletglance.data.makingRecord

import androidx.compose.runtime.Stable
import com.ataglance.walletglance.data.categories.CategoryWithSubcategory
import com.ataglance.walletglance.data.utils.formatWithSpaces

@Stable
data class MakeRecordUnitUiState(
    val lazyListKey: Int,
    val index: Int,
    val categoryWithSubcategory: CategoryWithSubcategory?,
    val note: String = "",
    val amount: String = "",
    val quantity: String = "",
    val collapsed: Boolean = true
) {
    fun getFormattedAmount(): String {
        return amount
            .takeIf { it.isNotBlank() && !(it.length == 1 && it.firstOrNull() == '.') }
            ?.toDouble()
            ?.formatWithSpaces()
            ?: return "------"
    }
}