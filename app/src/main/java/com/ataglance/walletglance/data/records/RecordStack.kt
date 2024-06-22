package com.ataglance.walletglance.data.records

import com.ataglance.walletglance.domain.entities.Category
import com.ataglance.walletglance.domain.entities.Record
import com.ataglance.walletglance.ui.utils.findById
import com.ataglance.walletglance.ui.utils.getOrderNumById
import com.ataglance.walletglance.ui.utils.getSubcategoryByIdAndParentOrderNum
import com.ataglance.walletglance.ui.viewmodels.records.MakeRecordUnitUiState
import java.util.Locale

data class RecordStack(
    val recordNum: Int,
    val date: Long,
    val type: Char,
    val accountId: Int,
    val totalAmount: Double,
    val stack: List<RecordStackUnit>
) {

    fun isExpense(): Boolean { return type == '-' }
    fun isIncome(): Boolean { return type == '+' }
    fun isOutTransfer(): Boolean { return type == '>' }
    fun isInTransfer(): Boolean { return type == '<' }
    fun isExpenseOrOutTransfer(): Boolean { return isExpense() || isOutTransfer() }
    fun isIncomeOrInTransfer(): Boolean { return isIncome() || isInTransfer() }
    fun isTransfer(): Boolean { return isOutTransfer() || isInTransfer() }

    fun isOfType(passedType: RecordType): Boolean {
        return (isExpense() && passedType == RecordType.Expense) ||
                (isIncome() && passedType == RecordType.Income)
    }

    fun getRecordType(): RecordType? {
        return when (type) {
            '-', '>' -> RecordType.Expense
            '+', '<' -> RecordType.Income
            else -> null
        }
    }

    fun toRecordList(): List<Record> {
        return stack.map { unit ->
            Record(
                id = unit.id,
                recordNum = recordNum,
                date = date,
                type = type,
                accountId = accountId,
                amount = unit.amount,
                quantity = unit.quantity,
                categoryId = unit.categoryId,
                subcategoryId = unit.subcategoryId,
                note = unit.note
            )
        }
    }

    fun toMakeRecordUnitList(
        categoryListAndSubcategoryLists: Pair<List<Category>, List<List<Category>>>
    ): List<MakeRecordUnitUiState> {
        return stack.mapIndexed { index, unit ->
            MakeRecordUnitUiState(
                lazyListKey = index,
                index = index,
                category = categoryListAndSubcategoryLists.first.findById(unit.categoryId),
                subcategory = unit.subcategoryId?.let { subcategoryId ->
                    categoryListAndSubcategoryLists.first.getOrderNumById(unit.categoryId)?.let {
                        categoryListAndSubcategoryLists.second
                            .getSubcategoryByIdAndParentOrderNum(subcategoryId, it)
                    }
                },
                note = unit.note ?: "",
                amount = "%.2f".format(Locale.US, unit.amount / (unit.quantity ?: 1)),
                quantity = unit.quantity?.toString() ?: "",
                collapsed = stack.size != 1
            )
        }
    }

    fun getFormattedAmountWithSpaces(currency: String?): String {
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

        return "%c %s %s".format(sign, formattedNumber, currency)
    }

}

data class RecordStackUnit(
    val id: Int = 0,
    val amount: Double,
    val quantity: Int?,
    val categoryId: Int,
    val subcategoryId: Int?,
    val note: String?
)