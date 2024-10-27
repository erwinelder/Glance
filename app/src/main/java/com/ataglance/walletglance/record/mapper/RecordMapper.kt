package com.ataglance.walletglance.record.mapper

import com.ataglance.walletglance.account.domain.Account
import com.ataglance.walletglance.account.domain.utils.findById
import com.ataglance.walletglance.category.domain.model.CategoriesWithSubcategories
import com.ataglance.walletglance.record.data.model.RecordEntity
import com.ataglance.walletglance.record.domain.RecordStack
import com.ataglance.walletglance.record.domain.RecordStackItem
import com.ataglance.walletglance.record.domain.utils.asChar
import com.ataglance.walletglance.record.domain.utils.asRecordType
import com.ataglance.walletglance.record.domain.utils.toCategoryTypeOrNullIfTransfer



fun Map<String, Any?>.toRecordEntity(): RecordEntity {
    return RecordEntity(
        id = this["id"] as Int,
        recordNum = this["recordNum"] as Int,
        date = this["date"] as Long,
        type = this["type"] as Char,
        accountId = this["accountId"] as Int,
        amount = this["amount"] as Double,
        quantity = this["quantity"] as Int?,
        categoryId = this["categoryId"] as Int,
        subcategoryId = this["subcategoryId"] as Int?,
        note = this["note"] as String?,
        includeInBudgets = this["includeInBudgets"] as Boolean
    )
}

fun RecordEntity.toMap(timestamp: Long): HashMap<String, Any?> {
    return hashMapOf(
        "LMT" to timestamp,
        "id" to id,
        "recordNum" to recordNum,
        "date" to date,
        "type" to type,
        "accountId" to accountId,
        "amount" to amount,
        "quantity" to quantity,
        "categoryId" to categoryId,
        "subcategoryId" to subcategoryId,
        "note" to note,
        "includeInBudgets" to includeInBudgets
    )
}



fun RecordEntity.toRecordStack(
    accountList: List<Account>,
    categoriesWithSubcategories: CategoriesWithSubcategories
): RecordStack? {
    val recordType = type.asRecordType() ?: return null
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
): RecordStackItem? {
    val recordType = type.asRecordType() ?: return null

    val categoryWithSubcategories = recordType.toCategoryTypeOrNullIfTransfer()?.let {
        categoriesWithSubcategories.findById(id = categoryId, type = it)
    }

    return RecordStackItem(
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



fun RecordStack.toRecordList(): List<RecordEntity> {
    return stack.map { unit ->
        RecordEntity(
            id = unit.id,
            recordNum = recordNum,
            date = date,
            type = type.asChar(),
            accountId = account.id,
            amount = unit.amount,
            quantity = unit.quantity,
            categoryId = unit.categoryWithSubcategory?.category?.id ?: 0,
            subcategoryId = unit.categoryWithSubcategory?.subcategory?.id,
            note = unit.note,
            includeInBudgets = unit.includeInBudgets
        )
    }
}
