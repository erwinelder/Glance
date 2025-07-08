package com.ataglance.walletglance.transaction.presentation.model

import com.ataglance.walletglance.account.domain.model.RecordAccount
import com.ataglance.walletglance.category.domain.model.CategoryType

data class RecordUiState(
    val id: Long,
    val date: String,
    val type: CategoryType,
    val account: RecordAccount,
    val totalAmount: String,
    val items: List<RecordItemUiState>
) : TransactionUiState()
