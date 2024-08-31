package com.ataglance.walletglance.recordCreation.domain

import androidx.compose.runtime.Stable
import com.ataglance.walletglance.category.domain.CategoryWithSubcategory
import com.ataglance.walletglance.core.utils.formatWithSpaces

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

    fun getTotalAmount(): Double? {
        return amount.toDoubleOrNull()?.let { amountDouble ->
            amountDouble * (quantity.toIntOrNull() ?: 1)
        }
    }

}