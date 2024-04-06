package com.ataglance.walletglance.model

import com.ataglance.walletglance.data.Account
import com.ataglance.walletglance.data.Record
import java.util.Locale

enum class RecordType {
    Expense, Income
}

class RecordController {

    fun getLastUsedCategoryPairByAccountId(
        accountId: Int,
        recordStackList: List<RecordStack>
    ): Pair<Int, Int?>? {
        return recordStackList.find { it.accountId == accountId }?.stack?.firstOrNull()?.let {
            it.categoryId to it.subcategoryId
        }
    }

    fun includeYearToRecordDate(recordStackList: List<RecordStack>): Boolean {
        return if (recordStackList.isNotEmpty()) {
            recordStackList.first().date / 100000000 == recordStackList.last().date / 100000000
        } else {
            false
        }
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

    fun convertRecordListToRecordStackList(recordList: List<Record>): List<RecordStack> {
        val recordStackList = mutableListOf<RecordStack>()

        recordList.forEach { record ->
            if (recordStackList.isEmpty() || recordStackList.lastOrNull()?.recordNum != record.recordNum) {
                recordStackList.add(RecordStack(
                    recordNum = record.recordNum,
                    date = record.date,
                    type = record.type,
                    accountId = record.accountId,
                    totalAmount = record.amount,
                    stack = listOf(record.toRecordStackUnit())
                ))
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

    fun convertRecordStackToMakeRecordStateAndUnitList(
        makeRecordStatus: String?,
        recordNum: Int?,
        accountList: List<Account>,
        activeAccount: Account?,
        recordStackList: List<RecordStack>,
        categoriesUiState: CategoriesUiState
    ): Pair<MakeRecordUiState, List<MakeRecordUnitUiState>?> {

        if (makeRecordStatus == MakeRecordStatus.Edit.name && recordNum != null && recordNum != 0) {
            val recordStack = recordStackList.find { it.recordNum == recordNum }

            recordStack?.let {
                return MakeRecordUiState(
                    recordStatus = MakeRecordStatus.Edit,
                    recordNum = recordStack.recordNum,
                    account = AccountController().getAccountById(it.accountId, accountList),
                    type = recordStack.getRecordType() ?: RecordType.Expense,
                    dateTimeState = DateTimeController().getNewDateByRecordLongDate(it.date)
                ) to recordStack.stackToMakeRecordUnitList(
                    categoriesUiState
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

    fun convertRecordStackToMakeTransferState(
        makeRecordStatus: String?,
        recordNum: Int?,
        accountList: List<Account>,
        activeAccount: Account?,
        recordStackList: List<RecordStack>
    ): MakeTransferUiState {
        if (makeRecordStatus == MakeRecordStatus.Edit.name && recordNum != null && recordNum != 0) {

            val firstRecordStack = recordStackList.find { it.recordNum == recordNum }
            val secondRecordStack = recordStackList.find {
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
                    recordStackFrom, recordStackTo, recordNum, accountList
                )
            }

        }

        return MakeTransferUiState(
            recordStatus = MakeRecordStatus.Create,
            fromAccount = activeAccount,
            toAccount = activeAccount?.let {
                AccountController().getAccountAnotherFrom(it, accountList)
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
            fromAccount = AccountController().getAccountById(
                recordStackFrom.accountId, accountList
            ),
            toAccount = AccountController().getAccountById(
                recordStackTo.accountId, accountList
            ),
            startAmount = "%.2f".format(Locale.US, recordStackFrom.totalAmount),
            finalAmount = "%.2f".format(Locale.US, recordStackTo.totalAmount),
            startRate = "%.2f".format(Locale.US, startAndFinalRate.first),
            finalRate = "%.2f".format(Locale.US, startAndFinalRate.second),
            dateTimeState = DateTimeController().getNewDateByRecordLongDate(
                recordStackFrom.date
            ),
            idFrom = recordStackFrom.stack.firstOrNull()?.id ?: 0,
            idTo = recordStackTo.stack.firstOrNull()?.id ?: 0
        )
    }

    fun convertTransfersToRecordsWithCategoryTransfer(transferList: List<Record>): List<Record> {
        val recordList = mutableListOf<Record>()

        transferList.forEach { transfer ->
            recordList.add(
                transfer.copy(
                    type = if (transfer.isOutTransfer()) '-' else '+',
                    categoryId = if (transfer.isOutTransfer()) 12 else 77,
                    subcategoryId = if (transfer.isOutTransfer()) 66 else null
                )
            )
        }

        return recordList
    }

}