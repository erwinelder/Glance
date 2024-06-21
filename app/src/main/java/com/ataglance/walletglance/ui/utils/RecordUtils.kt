package com.ataglance.walletglance.ui.utils

import com.ataglance.walletglance.data.app.MakeRecordStatus
import com.ataglance.walletglance.data.categories.CategoriesLists
import com.ataglance.walletglance.data.records.RecordStack
import com.ataglance.walletglance.data.records.RecordType
import com.ataglance.walletglance.domain.entities.Account
import com.ataglance.walletglance.domain.entities.Record
import com.ataglance.walletglance.ui.viewmodels.AccountsUiState
import com.ataglance.walletglance.ui.viewmodels.records.MakeRecordUiState
import com.ataglance.walletglance.ui.viewmodels.records.MakeRecordUnitUiState
import com.ataglance.walletglance.ui.viewmodels.records.MakeTransferUiState
import java.util.Locale


fun RecordType.inverse(): RecordType {
    return if (this == RecordType.Expense) RecordType.Income else RecordType.Expense
}


fun List<RecordStack>.findByRecordNum(recordNum: Int): RecordStack? {
    return this.find { it.recordNum == recordNum }
}


fun List<RecordStack>.needToIncludeYearToDate(): Boolean {
    return this.isNotEmpty() &&
            this.first().date / 100000000 == this.last().date / 100000000
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


fun List<RecordStack>.getLastUsedCategoryByAccountId(accountId: Int): Pair<Int, Int?>? {
    return this
        .find { it.accountId == accountId }
        ?.stack?.firstOrNull()
        ?.let { it.categoryId to it.subcategoryId }
}


fun List<Record>.toRecordStackList(): List<RecordStack> {
    val recordStackList = mutableListOf<RecordStack>()

    this.forEach { record ->
        if (recordStackList.lastOrNull()?.recordNum != record.recordNum) {
            recordStackList.add(record.toRecordStack())
        } else {
            val lastRecordStack = recordStackList.last()
            val stack = lastRecordStack.stack.toMutableList()
            val recordStackUnit = record.toRecordStackUnit()

            stack.add(recordStackUnit)
            recordStackList[recordStackList.lastIndex] = lastRecordStack.copy(
                totalAmount = lastRecordStack.totalAmount + recordStackUnit.amount,
                stack = stack
            )
        }
    }

    return recordStackList
}


fun List<RecordStack>.getMakeRecordStateAndUnitList(
    makeRecordStatus: MakeRecordStatus,
    recordNum: Int?,
    accountList: List<Account>,
    activeAccount: Account?,
    categoriesLists: CategoriesLists
): Pair<MakeRecordUiState, List<MakeRecordUnitUiState>?> {

    if (makeRecordStatus == MakeRecordStatus.Edit && recordNum != null && recordNum != 0) {
        this.find { it.recordNum == recordNum }?.let { recordStack ->
            return MakeRecordUiState(
                recordStatus = MakeRecordStatus.Edit,
                recordNum = recordStack.recordNum,
                account = accountList.findById(recordStack.accountId),
                type = recordStack.getRecordType() ?: RecordType.Expense,
                dateTimeState = getNewDateByRecordLongDate(recordStack.date)
            ) to recordStack.toMakeRecordUnitList(
                categoriesLists
                    .getParCategoryListAndSubcategoryListsByType(recordStack.getRecordType())
            )
        }
    }

    return MakeRecordUiState(
        recordStatus = MakeRecordStatus.Create,
        recordNum = null,
        account = activeAccount
    ) to null

}


fun List<RecordStack>.getMakeTransferState(
    makeRecordStatus: String?,
    recordNum: Int?,
    accountsUiState: AccountsUiState
): MakeTransferUiState {
    if (makeRecordStatus == MakeRecordStatus.Edit.name && recordNum != null && recordNum != 0) {

        val firstRecordStack = this.find { it.recordNum == recordNum }
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
            return convertRecordStacksFromAndToToMakeTransferUiState(
                recordStackFrom, recordStackTo, recordNum, accountsUiState.accountList
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


private fun convertRecordStacksFromAndToToMakeTransferUiState(
    recordStackFrom: RecordStack,
    recordStackTo: RecordStack,
    recordNum: Int,
    accountList: List<Account>
): MakeTransferUiState {
    val startAndFinalRate = getStartAndFinalRateByAmounts(
        recordStackFrom.totalAmount, recordStackTo.totalAmount
    )
    return MakeTransferUiState(
        recordNum = recordNum,
        recordStatus = MakeRecordStatus.Edit,
        fromAccount = accountList.findById(recordStackFrom.accountId),
        toAccount = accountList.findById(recordStackTo.accountId),
        startAmount = "%.2f".format(Locale.US, recordStackFrom.totalAmount),
        finalAmount = "%.2f".format(Locale.US, recordStackTo.totalAmount),
        startRate = "%.2f".format(Locale.US, startAndFinalRate.first),
        finalRate = "%.2f".format(Locale.US, startAndFinalRate.second),
        dateTimeState = getNewDateByRecordLongDate(
            recordStackFrom.date
        ),
        idFrom = recordStackFrom.stack.firstOrNull()?.id ?: 0,
        idTo = recordStackTo.stack.firstOrNull()?.id ?: 0
    )
}


fun List<Record>.transfersToRecordsWithCategoryOfTransfer(): List<Record> {
    return this.map { transferRecord ->
        transferRecord.copy(
            type = if (transferRecord.isOutTransfer()) '-' else '+',
            categoryId = if (transferRecord.isOutTransfer()) 12 else 77,
            subcategoryId = if (transferRecord.isOutTransfer()) 66 else null
        )
    }
}


fun List<Record>.getTransferSecondUnitsRecordNumbers(): List<Int> {
    return this.map { record ->
        if (record.isOutTransfer()) record.recordNum + 1 else record.recordNum - 1
    }
}

fun List<MakeRecordUnitUiState>.getTotalAmount(): Double {
    return this.fold(0.0) { total, recordUnit ->
        total + (recordUnit.amount.toDouble() * recordUnit.quantity.ifBlank { "1" }.toInt())
    }
}
