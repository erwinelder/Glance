package com.ataglance.walletglance.record.domain

import androidx.compose.runtime.Stable
import com.ataglance.walletglance.account.domain.RecordAccount
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.core.utils.formatWithSpaces

@Stable
data class RecordStack(
    val recordNum: Int,
    val date: Long,
    val type: RecordType,
    val account: RecordAccount,
    val totalAmount: Double,
    val stack: List<RecordStackItem>
) {

    fun isExpense() = type == RecordType.Expense
    fun isIncome() = type == RecordType.Income
    fun isOutTransfer() = type == RecordType.OutTransfer
    private fun isInTransfer() = type == RecordType.InTransfer
    fun isExpenseOrOutTransfer() = isExpense() || isOutTransfer()
    fun isIncomeOrInTransfer() = isIncome() || isInTransfer()
    fun isExpenseOrIncome() = isExpense() || isIncome()
    fun isTransfer() = isOutTransfer() || isInTransfer()

    fun isOfType(type: RecordType): Boolean {
        return this.type == type
    }
    fun isExplicitlyOfType(type: CategoryType): Boolean {
        return type == CategoryType.Expense && isExpense() ||
                type == CategoryType.Income && isIncome()
    }

    private fun getSign(): Char {
        return if (isExpenseOrOutTransfer()) '-' else '+'
    }

    fun getFormattedAmountWithSpaces(): String {
        return "%c %s %s".format(getSign(), totalAmount.formatWithSpaces(), account.currency)
    }

}