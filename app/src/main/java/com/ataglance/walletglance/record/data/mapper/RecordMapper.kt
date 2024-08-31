package com.ataglance.walletglance.record.data.mapper

import com.ataglance.walletglance.account.domain.Account
import com.ataglance.walletglance.account.utils.findById
import com.ataglance.walletglance.category.domain.CategoriesWithSubcategories
import com.ataglance.walletglance.recordCreation.domain.MakeRecordUiState
import com.ataglance.walletglance.recordCreation.domain.MakeRecordUnitUiState
import com.ataglance.walletglance.record.data.local.model.RecordEntity
import com.ataglance.walletglance.record.domain.RecordStack
import com.ataglance.walletglance.record.domain.RecordStackUnit
import com.ataglance.walletglance.record.domain.RecordType
import com.ataglance.walletglance.record.utils.getRecordTypeByChar
import com.ataglance.walletglance.record.utils.toCategoryType
import java.util.Locale


fun RecordEntity.toRecordStack(
    accountList: List<Account>,
    categoriesWithSubcategories: CategoriesWithSubcategories
): RecordStack? {
    val recordType = getRecordTypeByChar(type) ?: return null
    val recordAccount = accountList.findById(accountId)?.toRecordAccount() ?: return null
    val recordStackUnit = toRecordStackUnit(categoriesWithSubcategories) ?: return null

    return RecordStack(
        recordNum = recordNum,
        date = date,
        type = recordType,
        account = recordAccount,
        totalAmount = amount,
        stack = listOf(recordStackUnit)
    )
}

fun RecordEntity.toRecordStackUnit(
    categoriesWithSubcategories: CategoriesWithSubcategories
): RecordStackUnit? {
    val recordType = getRecordTypeByChar(type) ?: return null

    val categoryWithSubcategories = recordType.toCategoryType()?.let {
        categoriesWithSubcategories.findById(id = categoryId, type = it)
    }

    return RecordStackUnit(
        id = id,
        amount = amount,
        quantity = quantity,
        categoryWithSubcategory = subcategoryId?.let {
            categoryWithSubcategories?.getWithSubcategoryWithId(it)
        } ?: categoryWithSubcategories?.getWithoutSubcategory(),
        note = note,
        includeInBudgets = includeInBudgets
    )
}

fun List<RecordEntity>.toRecordStackList(
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



fun MakeRecordUiState.toRecordList(unitList: List<MakeRecordUnitUiState>): List<RecordEntity> {
    val recordList = mutableListOf<RecordEntity>()

    unitList.forEach { unit ->
        if (account != null && unit.categoryWithSubcategory != null) {
            recordList.add(
                RecordEntity(
                    recordNum = recordNum,
                    date = dateTimeState.dateLong,
                    type = if (type == RecordType.Expense) '-' else '+',
                    amount = if (unit.quantity.isNotBlank()) {
                        "%.2f".format(
                            Locale.US,
                            unit.getTotalAmount() ?: 0.0
                        ).toDouble()
                    } else {
                        unit.amount.toDouble()
                    },
                    quantity = unit.quantity.ifBlank { null }?.toInt(),
                    categoryId = unit.categoryWithSubcategory.category.id,
                    subcategoryId = unit.categoryWithSubcategory.subcategory?.id,
                    accountId = account.id,
                    note = unit.note.ifBlank { null },
                    includeInBudgets = includeInBudgets
                )
            )
        }
    }

    return recordList
}

fun MakeRecordUiState.toRecordListWithOldIds(
    unitList: List<MakeRecordUnitUiState>,
    recordStack: RecordStack
): List<RecordEntity> {
    val recordList = mutableListOf<RecordEntity>()

    unitList.forEach { unit ->
        if (account != null && unit.categoryWithSubcategory != null) {
            recordList.add(
                RecordEntity(
                    id = recordStack.stack.getOrNull(unit.index)?.id ?: 0,
                    recordNum = recordStack.recordNum,
                    date = dateTimeState.dateLong,
                    type = if (type == RecordType.Expense) '-' else '+',
                    amount = if (unit.quantity.isNotBlank()) {
                        "%.2f".format(
                            Locale.US,
                            unit.getTotalAmount() ?: 0.0
                        ).toDouble()
                    } else {
                        unit.amount.toDouble()
                    },
                    quantity = unit.quantity.ifBlank { null }?.toInt(),
                    categoryId = unit.categoryWithSubcategory.category.id,
                    subcategoryId = unit.categoryWithSubcategory.subcategory?.id,
                    accountId = account.id,
                    note = unit.note.ifBlank { null },
                    includeInBudgets = includeInBudgets
                )
            )
        }
    }

    return recordList
}
