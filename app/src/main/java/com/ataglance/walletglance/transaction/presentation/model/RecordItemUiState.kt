package com.ataglance.walletglance.transaction.presentation.model

import com.ataglance.walletglance.category.domain.model.CategoryWithSub

data class RecordItemUiState(
    val categoryWithSub: CategoryWithSub?,
    val note: String?
)
