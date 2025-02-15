package com.ataglance.walletglance.record.mapper

import com.ataglance.walletglance.account.domain.mapper.toRecordAccount
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.utils.findById
import com.ataglance.walletglance.category.domain.model.GroupedCategoriesByType
import com.ataglance.walletglance.record.data.local.model.RecordEntity
import com.ataglance.walletglance.record.domain.model.Record
import com.ataglance.walletglance.record.domain.model.RecordStack
import com.ataglance.walletglance.record.domain.model.RecordStackItem
import com.ataglance.walletglance.record.domain.utils.asChar
import com.ataglance.walletglance.record.domain.utils.asRecordType
import com.ataglance.walletglance.record.domain.utils.toCategoryTypeOrNullIfTransfer


fun List<RecordEntity>.toRecordStacks(
    accounts: List<Account>,
    groupedCategoriesByType: GroupedCategoriesByType
): List<RecordStack> {
    return this.groupBy { it.recordNum }
        .mapNotNull { (_, records) ->
            records.toRecordStack(
                accounts = accounts, groupedCategoriesByType = groupedCategoriesByType
            )
        }
}

fun List<RecordEntity>.toRecordStack(
    accounts: List<Account>,
    groupedCategoriesByType: GroupedCategoriesByType
): RecordStack? {
    val record = this.firstOrNull() ?: return null
    val units = this.mapNotNull { it.toRecordStackUnit(groupedCategoriesByType) }

    val recordType = record.type.asRecordType() ?: return null
    val recordAccount = accounts.findById(record.accountId)?.toRecordAccount() ?: return null
    val amount = units.sumOf { it.amount }

    return RecordStack(
        recordNum = record.recordNum,
        date = record.date,
        type = recordType,
        account = recordAccount,
        totalAmount = amount,
        stack = units
    )
}

fun RecordEntity.toRecordStackUnit(
    groupedCategoriesByType: GroupedCategoriesByType
): RecordStackItem? {
    val categoryType = type.asRecordType()?.toCategoryTypeOrNullIfTransfer() ?: return null
    val categoryWithSubcategories = groupedCategoriesByType.findById(
        id = categoryId, type = categoryType
    )

    return RecordStackItem(
        id = id,
        amount = amount,
        quantity = quantity,
        categoryWithSub = categoryWithSubcategories
            ?.getWithSubcategoryWithIdOrWithoutSubcategory(id = subcategoryId),
        note = note,
        includeInBudgets = includeInBudgets
    )
}


fun RecordStack.toRecordEntities(): List<RecordEntity> {
    return stack.map { unit ->
        RecordEntity(
            id = unit.id,
            recordNum = recordNum,
            date = date,
            type = type.asChar(),
            accountId = account.id,
            amount = unit.amount,
            quantity = unit.quantity,
            categoryId = unit.categoryWithSub?.category?.id ?: 0,
            subcategoryId = unit.categoryWithSub?.subcategory?.id,
            note = unit.note,
            includeInBudgets = unit.includeInBudgets
        )
    }
}


fun List<RecordEntity>.toDomainModels(): List<Record> {
    return this.mapNotNull { it.toDomainModel() }
}

fun RecordEntity.toDomainModel(): Record? {
    val type = type.asRecordType() ?: return null

    return Record(
        id = id,
        recordNum = recordNum,
        date = date,
        type = type,
        accountId = id,
        amount = amount,
        quantity = quantity,
        categoryId = categoryId,
        subcategoryId = subcategoryId,
        note = note,
        includeInBudgets = includeInBudgets
    )
}
