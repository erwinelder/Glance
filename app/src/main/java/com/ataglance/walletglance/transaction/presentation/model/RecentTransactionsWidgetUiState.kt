package com.ataglance.walletglance.transaction.presentation.model

data class RecentTransactionsWidgetUiState(
    val firstRecord: TransactionUiState? = null,
    val secondRecord: TransactionUiState? = null,
    val thirdRecord: TransactionUiState? = null
) {

    companion object {

        fun fromTransactions(
            transactions: List<TransactionUiState>
        ): RecentTransactionsWidgetUiState {
            return RecentTransactionsWidgetUiState(
                firstRecord = transactions.getOrNull(0),
                secondRecord = transactions.getOrNull(1),
                thirdRecord = transactions.getOrNull(2)
            )
        }

    }


    fun isEmpty(): Boolean {
        return firstRecord == null && secondRecord == null && thirdRecord == null
    }

    fun asList(): List<TransactionUiState> {
        return listOfNotNull(firstRecord, secondRecord, thirdRecord)
    }

}