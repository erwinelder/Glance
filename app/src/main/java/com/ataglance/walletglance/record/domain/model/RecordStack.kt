package com.ataglance.walletglance.record.domain.model

import androidx.compose.runtime.Stable
import com.ataglance.walletglance.account.domain.model.RecordAccount
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.category.domain.model.CategoryWithSub
import com.ataglance.walletglance.core.utils.formatWithSpaces
import com.ataglance.walletglance.record.domain.utils.distinctByCategories

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
    fun isInTransfer() = type == RecordType.InTransfer
    fun isExpenseOrOutTransfer() = isExpense() || isOutTransfer()
    fun isIncomeOrInTransfer() = isIncome() || isInTransfer()
    fun isExpenseOrIncome() = isExpense() || isIncome()
    fun isTransfer() = isOutTransfer() || isInTransfer()
    fun isNotTransfer() = !isTransfer()

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


    fun shrinkToCompactView(): RecordStack {
        val stack = stack.distinctByCategories(3).map { item ->
            item.copy(note = item.categoryWithSub?.let { stack.foldNotesByCategory(it) })
        }

        return copy(stack = stack)
    }

    private fun List<RecordStackItem>.foldNotesByCategory(
        categoryWithSub: CategoryWithSub
    ): String? {
        return this
            .filter {
                it.categoryWithSub?.match(categoryWithSub) == true && it.note?.isNotBlank() == true
            }
            .map { it.note }
            .distinct()
            .joinToString()
            .takeIf { it.isNotBlank() }
    }

}