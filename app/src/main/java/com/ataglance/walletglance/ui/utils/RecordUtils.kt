package com.ataglance.walletglance.ui.utils

import com.ataglance.walletglance.data.accounts.Account
import com.ataglance.walletglance.data.categories.CategoriesWithSubcategories
import com.ataglance.walletglance.data.categories.CategoryType
import com.ataglance.walletglance.data.categories.CategoryWithSubcategory
import com.ataglance.walletglance.data.categoryCollections.CategoryCollectionType
import com.ataglance.walletglance.data.categoryCollections.CategoryCollectionWithIds
import com.ataglance.walletglance.data.records.MakeRecordStatus
import com.ataglance.walletglance.data.records.RecordStack
import com.ataglance.walletglance.data.records.RecordType
import com.ataglance.walletglance.domain.entities.Record
import com.ataglance.walletglance.ui.viewmodels.AccountsUiState
import com.ataglance.walletglance.ui.viewmodels.ExpensesIncomeWidgetUiState
import com.ataglance.walletglance.ui.viewmodels.records.MakeRecordUiState
import com.ataglance.walletglance.ui.viewmodels.records.MakeRecordUnitUiState
import com.ataglance.walletglance.ui.viewmodels.records.MakeTransferUiState
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


fun List<Record>.toRecordStackList(
    accountList: List<Account>,
    categoriesWithSubcategories: CategoriesWithSubcategories
): List<RecordStack> {
    val recordStackList = mutableListOf<RecordStack>()

    this.forEach { record ->
        if (recordStackList.lastOrNull()?.recordNum != record.recordNum) {
            record.toRecordStack(accountList, categoriesWithSubcategories)
                ?.let { recordStackList.add(it) }
        } else {
            recordStackList.lastOrNull()?.let { lastRecordStack ->
                record.toRecordStackUnit(categoriesWithSubcategories)?.let { recordStackUnit ->
                    val stack = lastRecordStack.stack.toMutableList()
                    stack.add(recordStackUnit)
                    recordStackList[recordStackList.lastIndex] = lastRecordStack.copy(
                        totalAmount = lastRecordStack.totalAmount + recordStackUnit.amount,
                        stack = stack
                    )
                }
            }
        }
    }

    return recordStackList
}


fun List<RecordStack>.findByOrderNum(recordNum: Int): RecordStack? {
    return this.find { it.recordNum == recordNum }
}


fun List<RecordStack>.containsRecordsFromDifferentYears(): Boolean {
    return this.isNotEmpty() &&
            this.first().date / 100000000 != this.last().date / 100000000
}


fun List<RecordStack>.filterByDateAndAccount(
    dateRangeFromAndTo: Pair<Long, Long>,
    activeAccount: Account?
): List<RecordStack> {
    return this.filter {
        it.date in dateRangeFromAndTo.first..dateRangeFromAndTo.second &&
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


fun List<RecordStack>.getTotalAmountByType(type: RecordType): Double {
    return this
        .filter {
            (it.isOfType(type)) ||
                    (it.isOutTransfer() && type == RecordType.Expense) ||
                    (it.isInTransfer() && type == RecordType.Income)
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
    val expensesTotal = this.getTotalAmountByType(RecordType.Expense)
    val incomeTotal = this.getTotalAmountByType(RecordType.Income)
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


fun List<RecordStack>.getOutAndInTransfersByOneRecordNum(
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
        idFrom = this.first.stack.firstOrNull()?.id ?: 0,
        idTo = this.second.stack.firstOrNull()?.id ?: 0
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
