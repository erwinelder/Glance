package com.ataglance.walletglance.record.mapper

import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.utils.findById
import com.ataglance.walletglance.category.domain.model.CategoriesWithSubcategories
import com.ataglance.walletglance.core.utils.convertToDoubleOrZero
import com.ataglance.walletglance.record.data.model.RecordEntity
import com.ataglance.walletglance.record.domain.RecordStack
import com.ataglance.walletglance.record.domain.RecordStackItem
import com.ataglance.walletglance.record.domain.utils.asChar
import com.ataglance.walletglance.record.domain.utils.asRecordType
import com.ataglance.walletglance.record.domain.utils.toCategoryTypeOrNullIfTransfer


fun Map<String, Any?>.toRecordEntity(): RecordEntity {
    return RecordEntity(
        id = (this["id"] as Long).toInt(),
        recordNum = (this["recordNum"] as Long).toInt(),
        date = this["date"] as Long,
        type = (this["type"] as String).toCharArray()[0],
        accountId = (this["accountId"] as Long).toInt(),
        amount = this["amount"].convertToDoubleOrZero(),
        quantity = (this["quantity"] as Long?)?.toInt(),
        categoryId = (this["categoryId"] as Long).toInt(),
        subcategoryId = (this["subcategoryId"] as Long?)?.toInt(),
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
