package com.ataglance.walletglance.record.presentation.model

import com.ataglance.walletglance.category.domain.model.CategoryWithSub
import com.ataglance.walletglance.core.utils.addZeroIfDotIsAtTheBeginning
import com.ataglance.walletglance.core.utils.formatWithSpaces
import java.util.Locale

data class RecordDraftItem(
    val id: Long = 0,
    val lazyListKey: Int = 0,
    val categoryWithSub: CategoryWithSub? = null,
    val note: String = "",
    val amount: String = "",
    val quantity: String = "",
    val collapsed: Boolean = false
) {

    companion object {

        fun asNew(categoryWithSub: CategoryWithSub?): RecordDraftItem {
            return RecordDraftItem(
                categoryWithSub = categoryWithSub
            )
        }

    }


    fun getFormattedAmountOrPlaceholder(): String {
        return amount
            .takeIf { it.isNotBlank() && !(it.length == 1 && it.firstOrNull() == '.') }
            ?.toDouble()
            ?.formatWithSpaces()
            ?: return "------"
    }

    fun getTotalAmount(): Double {
        val totalAmount = amount.toDoubleOrNull()?.let { amountDouble ->
            amountDouble * (quantity.toIntOrNull()?.takeIf { it != 0 } ?: 1)
        } ?: 0.0
        return "%.2f".format(locale = Locale.US, totalAmount).toDouble()
    }

    val savingIsAllowed: Boolean
        get() = amount.isNotBlank() &&
                amount.last() != '.' &&
                amount.addZeroIfDotIsAtTheBeginning().toDouble() != 0.0 &&
                categoryWithSub != null

}
