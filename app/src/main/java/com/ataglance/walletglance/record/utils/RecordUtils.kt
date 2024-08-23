package com.ataglance.walletglance.record.utils

import com.ataglance.walletglance.account.utils.findById
import com.ataglance.walletglance.account.utils.getOtherFrom
import com.ataglance.walletglance.core.utils.getNewDateByRecordLongDate
import com.ataglance.walletglance.account.domain.Account
import com.ataglance.walletglance.account.domain.AccountsUiState
import com.ataglance.walletglance.category.domain.CategoryType
import com.ataglance.walletglance.category.domain.CategoryWithSubcategory
import com.ataglance.walletglance.categoryCollection.domain.CategoryCollectionType
import com.ataglance.walletglance.categoryCollection.domain.CategoryCollectionWithIds
import com.ataglance.walletglance.core.domain.date.LongDateRange
import com.ataglance.walletglance.makingRecord.domain.MakeRecordStatus
import com.ataglance.walletglance.makingRecord.domain.MakeRecordUiState
import com.ataglance.walletglance.makingRecord.domain.MakeRecordUnitUiState
import com.ataglance.walletglance.record.domain.RecordStack
import com.ataglance.walletglance.record.domain.RecordType
import com.ataglance.walletglance.core.domain.widgets.ExpensesIncomeWidgetUiState
import com.ataglance.walletglance.makingRecord.presentation.viewmodel.MakeTransferUiState
import java.util.Locale


fun RecordType.inverse(): RecordType {
    return when (this) {
        RecordType.Expense -> RecordType.Income
        RecordType.Income -> RecordType.Expense
        RecordType.OutTransfer -> RecordType.InTransfer
        RecordType.InTransfer -> RecordType.OutTransfer
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


fun RecordType.toCategoryType(): CategoryType? {
    return when (this) {
        RecordType.Expense -> CategoryType.Expense
        RecordType.Income -> CategoryType.Income
        else -> null
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


fun List<RecordStack>.findByOrderNum(recordNum: Int): RecordStack? {
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
    return this.mapNotNull { recordStack ->
        recordStack.stack
            .filter { it.categoryWithSubcategory?.matchCollection(collection) == true }
            .takeIf { it.isNotEmpty() }
            ?.let { recordStack.copy(stack = it) }
    }
}


fun List<RecordStack>.getStackTotalAmountByType(type: CategoryType): Double {
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
    val expensesTotal = this.getStackTotalAmountByType(CategoryType.Expense)
    val incomeTotal = this.getStackTotalAmountByType(CategoryType.Income)
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


fun List<RecordStack>.getMakeRecordStateAndUnitList(
    makeRecordStatus: MakeRecordStatus,
    recordNum: Int,
    accountList: List<Account>
): Pair<MakeRecordUiState, List<MakeRecordUnitUiState>>? {

    if (makeRecordStatus == MakeRecordStatus.Edit) {
        this.findByOrderNum(recordNum)?.takeIf { it.isExpenseOrIncome() }?.let { recordStack ->
            return MakeRecordUiState(
                recordStatus = MakeRecordStatus.Edit,
                recordNum = recordStack.recordNum,
                account = accountList.findById(recordStack.account.id),
                type = recordStack.type,
                dateTimeState = getNewDateByRecordLongDate(recordStack.date)
            ) to recordStack.toMakeRecordUnitList()
        }
    }

    return null
}


fun List<RecordStack>.getMakeTransferState(
    makeRecordStatus: MakeRecordStatus,
    recordNum: Int,
    accountsUiState: AccountsUiState
): MakeTransferUiState {
    if (makeRecordStatus == MakeRecordStatus.Edit) {

        val firstRecordStack = this.findByOrderNum(recordNum)
        val secondRecordStack = this.find {
            it.recordNum == recordNum + if (firstRecordStack?.isOutTransfer() == true) 1 else -1
        }
        val recordStackFrom = firstRecordStack?.let {
            if (firstRecordStack.isOutTransfer()) firstRecordStack else secondRecordStack
        }
        val recordStackTo = firstRecordStack?.let {
            if (firstRecordStack.isOutTransfer()) secondRecordStack else firstRecordStack
        }

        if (recordStackFrom != null && recordStackTo != null) {
            return (recordStackFrom to recordStackTo).toMakeTransferUiState(
                recordNum, accountsUiState.accountList
            )
        }

    }

    return MakeTransferUiState(
        recordStatus = MakeRecordStatus.Create,
        fromAccount = accountsUiState.activeAccount,
        toAccount = accountsUiState.activeAccount?.let {
            accountsUiState.accountList.getOtherFrom(it)
        }
    )
}


fun List<RecordStack>.getOutAndInTransfersByRecordNum(
    recordNum: Int
): Pair<RecordStack, RecordStack>? {
    val first = this.findByOrderNum(recordNum) ?: return null
    val second = this.findByOrderNum(
        recordNum + if (first.isOutTransfer()) 1 else -1
    ) ?: return null
    return if (first.isOutTransfer()) first to second else second to first
}


private fun Pair<RecordStack, RecordStack>.toMakeTransferUiState(
    recordNum: Int,
    accountList: List<Account>
): MakeTransferUiState {
    val startAndFinalRate = getStartAndFinalRateByAmounts(
        this.first.totalAmount, this.second.totalAmount
    )
    return MakeTransferUiState(
        recordNum = recordNum,
        recordStatus = MakeRecordStatus.Edit,
        fromAccount = accountList.findById(this.first.account.id),
        toAccount = accountList.findById(this.second.account.id),
        startAmount = "%.2f".format(Locale.US, this.first.totalAmount),
        finalAmount = "%.2f".format(Locale.US, this.second.totalAmount),
        startRate = "%.2f".format(Locale.US, startAndFinalRate.first),
        finalRate = "%.2f".format(Locale.US, startAndFinalRate.second),
        dateTimeState = getNewDateByRecordLongDate(this.first.date),
        recordIdFrom = this.first.stack.firstOrNull()?.id ?: 0,
        recordIdTo = this.second.stack.firstOrNull()?.id ?: 0
    )
}


fun List<MakeRecordUnitUiState>.copyWithCategoryWithSubcategory(
    categoryWithSubcategory: CategoryWithSubcategory?
): List<MakeRecordUnitUiState> {
    return this.map { it.copy(categoryWithSubcategory = categoryWithSubcategory) }
}


fun List<MakeRecordUnitUiState>.getTotalAmount(): Double {
    return this.fold(0.0) { total, recordUnit ->
        total + (recordUnit.amount.toDouble() * recordUnit.quantity.ifBlank { "1" }.toInt())
    }
}


fun List<MakeRecordUnitUiState>.getTotalAmountByCategory(categoryId: Int): Double {
    return this
        .filter {
            it.categoryWithSubcategory?.subcategory?.id == categoryId ||
                    it.categoryWithSubcategory?.category?.id == categoryId
        }
        .fold(0.0) { total, recordUnit ->
            total + (recordUnit.getTotalAmount() ?: 0.0)
        }
}