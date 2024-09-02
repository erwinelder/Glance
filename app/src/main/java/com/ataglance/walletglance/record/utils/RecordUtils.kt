package com.ataglance.walletglance.record.utils

import androidx.annotation.StringRes
import com.ataglance.walletglance.R
import com.ataglance.walletglance.account.domain.Account
import com.ataglance.walletglance.account.domain.AccountsUiState
import com.ataglance.walletglance.account.utils.findById
import com.ataglance.walletglance.account.utils.getOtherFrom
import com.ataglance.walletglance.category.domain.CategoryType
import com.ataglance.walletglance.categoryCollection.domain.CategoryCollectionType
import com.ataglance.walletglance.categoryCollection.domain.CategoryCollectionWithIds
import com.ataglance.walletglance.core.domain.date.LongDateRange
import com.ataglance.walletglance.core.domain.widgets.ExpensesIncomeWidgetUiState
import com.ataglance.walletglance.core.utils.getNewDateByRecordLongDate
import com.ataglance.walletglance.record.domain.RecordStack
import com.ataglance.walletglance.record.domain.RecordType
import com.ataglance.walletglance.record.domain.RecordsTypeFilter
import com.ataglance.walletglance.recordCreation.domain.transfer.TransferDraft
import com.ataglance.walletglance.recordCreation.domain.transfer.TransferDraftSenderReceiver
import com.ataglance.walletglance.recordCreation.domain.transfer.TransferSenderReceiverRecordNums
import java.util.Locale


fun RecordType.inverse(): RecordType {
    return when (this) {
        RecordType.Expense -> RecordType.Income
        RecordType.Income -> RecordType.Expense
        RecordType.OutTransfer -> RecordType.InTransfer
        RecordType.InTransfer -> RecordType.OutTransfer
    }
}

fun RecordType.toCategoryType(): CategoryType? {
    return when (this) {
        RecordType.Expense -> CategoryType.Expense
        RecordType.Income -> CategoryType.Income
        else -> null
    }
}

fun RecordType.asChar(): Char {
    return when (this) {
        RecordType.Expense -> '-'
        RecordType.Income -> '+'
        RecordType.OutTransfer -> '>'
        RecordType.InTransfer -> '<'
    }
}

fun getRecordTypeByChar(char: Char): RecordType? {
    return when (char) {
        '-' -> RecordType.Expense
        '+' -> RecordType.Income
        '>' -> RecordType.OutTransfer
        '<' -> RecordType.InTransfer
        else -> null
    }
}



@StringRes fun RecordsTypeFilter.getNoRecordsMessageRes(): Int {
    return when (this) {
        RecordsTypeFilter.All -> R.string.you_have_no_records_in_date_range
        RecordsTypeFilter.Expenses -> R.string.you_have_no_expenses_in_date_range
        RecordsTypeFilter.Income -> R.string.you_have_no_income_in_date_range
    }
}



fun List<RecordStack>.findByRecordNum(recordNum: Int): RecordStack? {
    return this.find { it.recordNum == recordNum }
}



fun List<RecordStack>.containsRecordsFromDifferentYears(): Boolean {
    return this.isNotEmpty() &&
            this.first().date / 100000000 != this.last().date / 100000000
}



fun List<RecordStack>.filterByDateAndAccount(
    dateRange: LongDateRange,
    activeAccount: Account?
): List<RecordStack> {
    return this.filter {
        it.date in dateRange.from..dateRange.to &&
                it.account.id == activeAccount?.id
    }
}



fun List<RecordStack>.filterByCollectionType(type: CategoryCollectionType): List<RecordStack> {
    return when (type) {
        CategoryCollectionType.Mixed -> this
        CategoryCollectionType.Expense -> this.filter { it.isExpenseOrOutTransfer() }
        CategoryCollectionType.Income -> this.filter { it.isIncomeOrInTransfer() }
    }
}



fun List<RecordStack>.filterByCollection(collection: CategoryCollectionWithIds): List<RecordStack> {
    return this.filterByCollectionType(collection.type).let { recordStacksFilteredByType ->
        recordStacksFilteredByType.takeUnless { collection.hasLinkedCategories() }
            ?: recordStacksFilteredByType.filterByCategoriesIds(collection.categoriesIds!!)
    }
}



fun List<RecordStack>.filterByCategoriesIds(categoriesIds: List<Int>): List<RecordStack> {
    return this.mapNotNull { recordStack ->
        recordStack.stack
            .filter { it.categoryWithSubcategory?.matchCategoriesIds(categoriesIds) == true }
            .takeIf { it.isNotEmpty() }
            ?.let { recordStack.copy(stack = it) }
    }
}


fun List<RecordStack>.getTotalAmountByType(type: CategoryType): Double {
    return this
        .filter {
            type == CategoryType.Expense && it.isExpenseOrOutTransfer() ||
                    type == CategoryType.Income && it.isIncomeOrInTransfer()
        }
        .fold(0.0) { total, recordStack ->
            total + recordStack.totalAmount
        }
}


fun getTotalPercentages(expensesTotal: Double, incomeTotal: Double): Pair<Double, Double> {
    return (expensesTotal + incomeTotal)
        .let { if (it == 0.0) null else it }
        ?.let {
            (100 / it) * expensesTotal to (100 / it) * incomeTotal
        } ?: (0.0 to 0.0)
}


fun List<RecordStack>.getExpensesIncomeWidgetUiState(): ExpensesIncomeWidgetUiState {
    val expensesTotal = this.getTotalAmountByType(CategoryType.Expense)
    val incomeTotal = this.getTotalAmountByType(CategoryType.Income)
    val (expensesPercentage, incomePercentage) = getTotalPercentages(expensesTotal, incomeTotal)

    return ExpensesIncomeWidgetUiState(
        expensesTotal = expensesTotal,
        incomeTotal = incomeTotal,
        expensesPercentage = expensesPercentage,
        incomePercentage = incomePercentage,
        expensesPercentageFloat = (expensesPercentage / 100).toFloat(),
        incomePercentageFloat = (incomePercentage / 100).toFloat()
    )
}


private fun getStartAndFinalRateByAmounts(
    startAmount: Double,
    finalAmount: Double
): Pair<Double, Double> {
    return if (startAmount <= finalAmount) {
        1.0 to finalAmount / startAmount
    } else {
        startAmount / finalAmount to 1.0
    }
}


fun List<RecordStack>.getTransferDraft(
    isNew: Boolean,
    recordNum: Int,
    accountsUiState: AccountsUiState
): TransferDraft {
    if (!isNew) {
        getOutAndInTransfersByRecordNum(recordNum)?.let {
            return it.toTransferDraft(accountList = accountsUiState.accountList)
        }
    }

    return TransferDraft(
        isNew = isNew,
        sender = TransferDraftSenderReceiver(
            account = accountsUiState.activeAccount,
            recordNum = recordNum
        ),
        receiver = TransferDraftSenderReceiver(
            account = accountsUiState.activeAccount?.let {
                accountsUiState.accountList.getOtherFrom(it)
            },
            recordNum = recordNum + 1
        )
    )
}


fun List<RecordStack>.getOutAndInTransfersByRecordNum(
    recordNum: Int
): Pair<RecordStack, RecordStack>? {
    val first = this.findByRecordNum(recordNum) ?: return null
    val second = this.findByRecordNum(
        recordNum + if (first.isOutTransfer()) 1 else -1
    ) ?: return null
    return if (first.isOutTransfer()) first to second else second to first
}


fun List<RecordStack>.getOutAndInTransfersByRecordNums(
    senderReceiverRecordNums: TransferSenderReceiverRecordNums
): Pair<RecordStack, RecordStack>? {
    val first = this.findByRecordNum(senderReceiverRecordNums.sender) ?: return null
    val second = this.findByRecordNum(senderReceiverRecordNums.receiver) ?: return null
    return first to second
}


private fun Pair<RecordStack, RecordStack>.toTransferDraft(
    accountList: List<Account>
): TransferDraft {
    val startAndFinalRate = getStartAndFinalRateByAmounts(
        this.first.totalAmount, this.second.totalAmount
    )
    val sender = TransferDraftSenderReceiver(
        account = accountList.findById(this.first.account.id),
        recordNum = this.first.recordNum,
        recordId = this.first.stack.firstOrNull()?.id ?: 0,
        amount = "%.2f".format(Locale.US, this.first.totalAmount),
        rate = "%.2f".format(Locale.US, startAndFinalRate.first)
    )
    val receiver = TransferDraftSenderReceiver(
        account = accountList.findById(this.second.account.id),
        recordNum = this.second.recordNum,
        recordId = this.second.stack.firstOrNull()?.id ?: 0,
        amount = "%.2f".format(Locale.US, this.second.totalAmount),
        rate = "%.2f".format(Locale.US, startAndFinalRate.second)
    )
    return TransferDraft(
        isNew = false,
        sender = sender,
        receiver = receiver,
        dateTimeState = getNewDateByRecordLongDate(this.first.date),
        includeInBudgets = this.first.stack.firstOrNull()?.includeInBudgets ?: true,
        savingIsAllowed = sender.savingIsAllowed() && receiver.savingIsAllowed()
    )
}
