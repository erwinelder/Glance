package com.ataglance.walletglance.data.mappers

import com.ataglance.walletglance.domain.accounts.Account
import com.ataglance.walletglance.domain.categories.CategoriesWithSubcategories
import com.ataglance.walletglance.domain.categories.CategoryWithSubcategory
import com.ataglance.walletglance.data.local.entities.RecordEntity
import com.ataglance.walletglance.domain.makingRecord.MakeRecordUiState
import com.ataglance.walletglance.domain.makingRecord.MakeRecordUnitUiState
import com.ataglance.walletglance.domain.records.RecordStack
import com.ataglance.walletglance.domain.records.RecordStackUnit
import com.ataglance.walletglance.domain.records.RecordType
import com.ataglance.walletglance.domain.utils.findById
import com.ataglance.walletglance.domain.utils.getRecordTypeByChar
import com.ataglance.walletglance.domain.utils.toCategoryType
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
        categoriesWithSubcategories.findById(categoryId, it)
    }

    return RecordStackUnit(
        id = id,
        amount = amount,
        quantity = quantity,
        categoryWithSubcategory = subcategoryId?.let {
            categoryWithSubcategories?.getWithSubcategoryWithId(it)
        } ?: categoryWithSubcategories?.category?.let { CategoryWithSubcategory(it) },
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
