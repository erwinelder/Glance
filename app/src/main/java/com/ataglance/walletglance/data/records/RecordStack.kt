package com.ataglance.walletglance.data.records

import com.ataglance.walletglance.data.accounts.RecordAccount
import com.ataglance.walletglance.data.categories.Category
import com.ataglance.walletglance.domain.entities.Record
import com.ataglance.walletglance.ui.utils.asChar
import com.ataglance.walletglance.ui.viewmodels.records.MakeRecordUnitUiState
import java.util.Locale

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
    fun isInTransfer() = type == RecordType.InTransfer
    fun isExpenseOrOutTransfer() = isExpense() || isOutTransfer()
    fun isIncomeOrInTransfer() = isIncome() || isInTransfer()
    fun isExpenseOrIncome() = isExpense() || isIncome()
    fun isTransfer() = isOutTransfer() || isInTransfer()

    fun isOfType(passedType: RecordType): Boolean {
        return (isExpense() && passedType == RecordType.Expense) ||
                (isIncome() && passedType == RecordType.Income)
    }

    fun toRecordList(): List<Record> {
        return stack.map { unit ->
            Record(
                id = unit.id,
                recordNum = recordNum,
                date = date,
                type = type.asChar(),
                accountId = account.id,
                amount = unit.amount,
                quantity = unit.quantity,
                categoryId = unit.category?.id ?: 0,
                subcategoryId = unit.subcategory?.id,
                note = unit.note
            )
        }
    }

    fun toMakeRecordUnitList(): List<MakeRecordUnitUiState> {
        return stack.mapIndexed { index, unit ->
            MakeRecordUnitUiState(
                lazyListKey = index,
                index = index,
                category = unit.category,
                subcategory = unit.subcategory,
                note = unit.note ?: "",
                amount = "%.2f".format(Locale.US, unit.amount / (unit.quantity ?: 1)),
                quantity = unit.quantity?.toString() ?: "",
                collapsed = stack.size != 1
            )
        }
    }

    fun getFormattedAmountWithSpaces(): String {
        var numberString = "%.2f".format(Locale.US, totalAmount)
        var formattedNumber = numberString.let {
            it.substring(startIndex = it.length - 3)
        }
        numberString = numberString.let {
            it.substring(0, it.length - 3)
        }
        var digitCount = 0

        for (i in numberString.length - 1 downTo 0) {
            formattedNumber = numberString[i] + formattedNumber
            digitCount++
            if (digitCount % 3 == 0 && i != 0) {
                formattedNumber = " $formattedNumber"
            }
        }

        val sign = if (isExpenseOrOutTransfer()) '-' else '+'

        return "%c %s %s".format(sign, formattedNumber, account.currency)
    }

}

data class RecordStackUnit(
    val id: Int = 0,
    val amount: Double,
    val quantity: Int?,
    val category: Category?,
    val subcategory: Category?,
    val note: String?
)