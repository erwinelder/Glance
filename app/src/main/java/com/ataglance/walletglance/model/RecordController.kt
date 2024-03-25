package com.ataglance.walletglance.model

import com.ataglance.walletglance.data.Account
import com.ataglance.walletglance.data.Category
import com.ataglance.walletglance.data.Record

enum class RecordType {
    Expense, Income
}

class RecordController {

    private fun Record.toRecordStackUnit(): RecordStackUnit {
        return RecordStackUnit(
            id = this.id,
            amount = this.amount,
            quantity = this.quantity,
            categoryId = this.categoryId,
            subcategoryId = this.subcategoryId,
            note = this.note
        )
    }

    fun getLastUsedCategoryPairByAccountId(accountId: Int, recordStackList: List<RecordStack>): Pair<Int, Int?>? {
        recordStackList.forEach { recordStack ->
            if (recordStack.accountId == accountId) {
                return recordStack.stack.first().categoryId to recordStack.stack.first().subcategoryId
            }
        }
        return null
    }

    fun formatRecordDateForHistory(date: Long, includeYear: Boolean): String {
        val year = (date / 100000000).toInt()
        val month = date / 1000000 - year * 100
        val day = date / 10000 - year * 10000 - month * 100

        return if (includeYear) "$day.$month.$year" else "$day.$month"
    }

    fun includeYearToRecordDate(recordStackList: List<RecordStack>): Boolean {
        return if (recordStackList.isNotEmpty()) {
            recordStackList.first().date / 100000000 == recordStackList.last().date / 100000000
        } else {
            false
        }
    }

    fun isTransfer(type: Char): Boolean {
        return type == '>' || type == '<'
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

    private fun getParCategoryListAndSubcategoryListsByType(
        type: RecordType?,
        categoriesUiState: CategoriesUiState
    ): Pair<List<Category>, List<List<Category>>> {
        return if (
            type == RecordType.Expense
        ) {
            categoriesUiState.parentCategories.expense
        } else {
            categoriesUiState.parentCategories.income
        } to
                if (
                    type == RecordType.Expense
                ) {
                    categoriesUiState.subcategories.expense
                } else {
                    categoriesUiState.subcategories.income
                }
    }

    fun recordTypeCharToRecordType(type: Char): RecordType? {
        return when (type) {
            '-', '>' -> RecordType.Expense
            '+', '<' -> RecordType.Income
            else -> null
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

    fun convertRecordStackToRecordList(recordStack: RecordStack): List<Record> {
        return recordStack.stack.map { unit ->
            Record(
                id = unit.id,
                recordNum = recordStack.recordNum,
                date = recordStack.date,
                type = recordStack.type,
                accountId = recordStack.accountId,
                amount = unit.amount,
                quantity = unit.quantity,
                categoryId = unit.categoryId,
                subcategoryId = unit.subcategoryId,
                note = unit.note
            )
        }
    }

    fun convertMakeRecordStateAndUnitListToRecordList(
        uiState: MakeRecordUiState,
        unitList: List<MakeRecordUnitUiState>,
        lastRecordNum: Int
    ): List<Record> {
        val recordList = mutableListOf<Record>()

        unitList.forEach { unit ->
            if (uiState.account != null && unit.category != null) {
                recordList.add(
                    Record(
                        recordNum = uiState.recordNum ?: (lastRecordNum + 1),
                        date = uiState.dateTimeState.dateLong,
                        type = if (uiState.type == RecordType.Expense) '-' else '+',
                        amount = if (unit.quantity.isNotBlank()) {
                            "%.2f".format(
                                unit.amount.toDouble() * unit.quantity.toInt()
                            ).toDouble()
                        } else {
                            unit.amount.toDouble()
                        },
                        quantity = unit.quantity.ifBlank { null }?.toInt(),
                        categoryId = unit.category.id,
                        subcategoryId = unit.subcategory?.id,
                        accountId = uiState.account.id,
                        note = unit.note.ifBlank { null }
                    )
                )
            }
        }

        return recordList
    }

    fun convertMakeRecordStateAndUnitListToRecordListWithOldIds(
        uiState: MakeRecordUiState,
        unitList: List<MakeRecordUnitUiState>,
        recordStack: RecordStack
    ): List<Record> {
        val recordList = mutableListOf<Record>()

        unitList.forEach { unit ->
            if (uiState.account != null && unit.category != null) {
                recordList.add(
                    Record(
                        id = recordStack.stack[unit.index].id,
                        recordNum = recordStack.recordNum,
                        date = uiState.dateTimeState.dateLong,
                        type = if (uiState.type == RecordType.Expense) '-' else '+',
                        amount = if (unit.quantity.isNotBlank()) {
                            "%.2f".format(
                                unit.amount.toDouble() * unit.quantity.toInt()
                            ).toDouble()
                        } else {
                            unit.amount.toDouble()
                        },
                        quantity = unit.quantity.ifBlank { null }?.toInt(),
                        categoryId = unit.category.id,
                        subcategoryId = unit.subcategory?.id,
                        accountId = uiState.account.id,
                        note = unit.note.ifBlank { null }
                    )
                )
            }
        }

        return recordList
    }

    fun convertMadeTransferStateToRecordsPair(uiState: MadeTransferState): Pair<Record, Record> {
        return Pair(
            Record(
                id = uiState.idFrom,
                recordNum = uiState.recordNum,
                date = uiState.dateTimeState.dateLong,
                type = '>',
                amount = uiState.startAmount,
                quantity = null,
                categoryId = 0,
                subcategoryId = null,
                accountId = uiState.fromAccount.id,
                note = uiState.toAccount.id.toString()
            ),
            Record(
                id = uiState.idTo,
                recordNum = uiState.recordNum + 1,
                date = uiState.dateTimeState.dateLong,
                type = '<',
                amount = uiState.finalAmount,
                quantity = null,
                categoryId = 0,
                subcategoryId = null,
                accountId = uiState.toAccount.id,
                note = uiState.fromAccount.id.toString()
            )
        )
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
                    type = recordTypeCharToRecordType(it.type) ?: RecordType.Expense,
                    dateTimeState = DateTimeController().getNewDateByRecordLongDate(it.date)
                ) to convertRecordStackUnitListToMakeRecordUnitList(
                    recordStackUnitList = recordStack.stack,
                    categoryListAndSubcategoryLists = getParCategoryListAndSubcategoryListsByType(
                        recordTypeCharToRecordType(recordStack.type),
                        categoriesUiState
                    )
                )
            }
        }

        return MakeRecordUiState(
            recordStatus = MakeRecordStatus.Create,
            recordNum = null,
            account = activeAccount
        ) to null

    }

    private fun convertRecordStackUnitListToMakeRecordUnitList(
        recordStackUnitList: List<RecordStackUnit>,
        categoryListAndSubcategoryLists: Pair<List<Category>, List<List<Category>>>
    ): List<MakeRecordUnitUiState> {
        val makeRecordUnitList = mutableListOf<MakeRecordUnitUiState>()

        recordStackUnitList.forEach { unit ->
            makeRecordUnitList.add(
                MakeRecordUnitUiState(
                    index = makeRecordUnitList.lastIndex + 1,
                    category = CategoryController().getParCategoryFromList(
                        id = unit.categoryId,
                        list = categoryListAndSubcategoryLists.first
                    ),
                    subcategory = unit.subcategoryId?.let { subcategoryId ->
                        CategoryController().getCategoryOrderNumById(
                            id = unit.categoryId,
                            list = categoryListAndSubcategoryLists.first
                        )?.let { parCategoryOrderNum ->
                            CategoryController().getSubcategByParCategOrderNumFromLists(
                                subcategoryId = subcategoryId,
                                parentCategoryOrderNum = parCategoryOrderNum,
                                lists = categoryListAndSubcategoryLists.second
                            )
                        }
                    },
                    note = unit.note ?: "",
                    amount = "%.2f".format(unit.amount / (unit.quantity ?: 1)),
                    quantity = unit.quantity?.toString() ?: ""
                )
            )
        }

        return makeRecordUnitList
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
                it.recordNum == recordNum + if (firstRecordStack?.type == '>') 1 else -1
            }
            val recordStackFrom = firstRecordStack?.let {
                if (firstRecordStack.type == '>') firstRecordStack else secondRecordStack
            }
            val recordStackTo = firstRecordStack?.let {
                if (firstRecordStack.type == '>') secondRecordStack else firstRecordStack
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
            startAmount = "%.2f".format(recordStackFrom.totalAmount),
            finalAmount = "%.2f".format(recordStackTo.totalAmount),
            startRate = "%.2f".format(startAndFinalRate.first),
            finalRate = "%.2f".format(startAndFinalRate.second),
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
                    type = if (transfer.type == '>') '-' else '+',
                    categoryId = if (transfer.type == '>') 12 else 77,
                    subcategoryId = if (transfer.type == '>') 66 else null
                )
            )
        }

        return recordList
    }

}