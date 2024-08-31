package com.ataglance.walletglance.record.domain

import androidx.compose.runtime.Stable
import com.ataglance.walletglance.account.domain.RecordAccount
import com.ataglance.walletglance.core.utils.formatWithSpaces
import com.ataglance.walletglance.record.data.local.model.RecordEntity
import com.ataglance.walletglance.record.utils.asChar
import com.ataglance.walletglance.recordCreation.domain.MakeRecordUnitUiState
import java.util.Locale

@Stable
data class RecordStack(
    val recordNum: Int,
    val date: Long,
    val type: RecordType,
    val account: RecordAccount,
    val totalAmount: Double,
    val stack: List<RecordStackUnit>
) {

    private fun isExpense() = type == RecordType.Expense
    private fun isIncome() = type == RecordType.Income
    fun isOutTransfer() = type == RecordType.OutTransfer
    private fun isInTransfer() = type == RecordType.InTransfer
    fun isExpenseOrOutTransfer() = isExpense() || isOutTransfer()
    fun isIncomeOrInTransfer() = isIncome() || isInTransfer()
    fun isExpenseOrIncome() = isExpense() || isIncome()
    fun isTransfer() = isOutTransfer() || isInTransfer()

    fun isOfType(passedType: RecordType): Boolean {
        return type == passedType
    }

    fun toRecordList(): List<RecordEntity> {
        return stack.map { unit ->
            RecordEntity(
                id = unit.id,
                recordNum = recordNum,
                date = date,
                type = type.asChar(),
                accountId = account.id,
                amount = unit.amount,
                quantity = unit.quantity,
                categoryId = unit.categoryWithSubcategory?.category?.id ?: 0,
                subcategoryId = unit.categoryWithSubcategory?.subcategory?.id,
                note = unit.note,
                includeInBudgets = unit.includeInBudgets
            )
        }
    }

    fun toMakeRecordUnitList(): List<MakeRecordUnitUiState> {
        return stack.mapIndexed { index, unit ->
            MakeRecordUnitUiState(
                lazyListKey = index,
                index = index,
                categoryWithSubcategory = unit.categoryWithSubcategory,
                note = unit.note ?: "",
                amount = "%.2f".format(
                    Locale.US,
                    unit.amount / (unit.quantity.takeUnless { it == 0 } ?: 1)
                ),
                quantity = unit.quantity?.toString() ?: "",
                collapsed = stack.size != 1
            )
        }
    }

    private fun getSign(): Char {
        return if (isExpenseOrOutTransfer()) '-' else '+'
    }

    fun getFormattedAmountWithSpaces(): String {
        return "%c %s %s".format(getSign(), totalAmount.formatWithSpaces(), account.currency)
    }

}