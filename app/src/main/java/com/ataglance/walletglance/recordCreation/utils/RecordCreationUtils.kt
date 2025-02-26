package com.ataglance.walletglance.recordCreation.utils

import com.ataglance.walletglance.category.domain.model.CategoryWithSub
import com.ataglance.walletglance.recordCreation.domain.record.CreatedRecordItem
import com.ataglance.walletglance.recordCreation.presentation.model.record.RecordDraftItem


fun List<RecordDraftItem>.copyWithCategoryAndSubcategory(
    categoryWithSub: CategoryWithSub?
): List<RecordDraftItem> {
    return this.map { it.copy(categoryWithSub = categoryWithSub) }
}

fun List<CreatedRecordItem>.getTotalAmount(): Double {
    return this.fold(0.0) { acc, item ->
        acc + item.totalAmount
    }
}
