package com.ataglance.walletglance.transaction.presentation.model

import com.ataglance.walletglance.account.domain.model.RecordAccount
import com.ataglance.walletglance.category.domain.model.CategoryType

data class TransferUiState(
    val id: Long,
    val date: String,
    val type: CategoryType,
    val account: RecordAccount,
    val secondAccountCompanionText: String,
    val secondAccount: RecordAccount,
    val amount: String
) : TransactionUiState()
