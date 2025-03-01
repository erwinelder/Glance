package com.ataglance.walletglance.recordCreation.mapper

import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.utils.findById
import com.ataglance.walletglance.category.domain.utils.asChar
import com.ataglance.walletglance.core.domain.date.DateTimeState
import com.ataglance.walletglance.record.data.local.model.RecordEntity
import com.ataglance.walletglance.record.domain.model.RecordStack
import com.ataglance.walletglance.record.domain.model.RecordStackItem
import com.ataglance.walletglance.record.domain.utils.toCategoryTypeOrNullIfTransfer
import com.ataglance.walletglance.recordCreation.domain.record.CreatedRecord
import com.ataglance.walletglance.recordCreation.domain.record.CreatedRecordItem
import com.ataglance.walletglance.recordCreation.domain.record.RecordDraftPreferences
import com.ataglance.walletglance.recordCreation.presentation.model.record.RecordDraft
import com.ataglance.walletglance.recordCreation.presentation.model.record.RecordDraftGeneral
import com.ataglance.walletglance.recordCreation.presentation.model.record.RecordDraftItem
import com.ataglance.walletglance.recordCreation.utils.getTotalAmount
import java.util.Locale


fun RecordStack.toRecordDraft(
    accounts: List<Account>
): RecordDraft? {
    val type = this.type.toCategoryTypeOrNullIfTransfer() ?: return null
    val includeInBudgets = this.stack.firstOrNull()?.includeInBudgets ?: return null

    return RecordDraft(
        general = RecordDraftGeneral(
            isNew = false,
            recordNum = this.recordNum,
            account = accounts.findById(this.account.id),
            type = type,
            dateTimeState = DateTimeState.fromTimestamp(this.date),
            preferences = RecordDraftPreferences(
                includeInBudgets = includeInBudgets
            )
        ),
        items = this.stack.toRecordDraftItems()
    )
}

private fun List<RecordStackItem>.toRecordDraftItems(): List<RecordDraftItem> {
    return this.mapIndexed { index, stackItem ->
        stackItem.toRecordDraftItem(index = index, collapsed = this.size != 1)
    }
}

private fun RecordStackItem.toRecordDraftItem(index: Int, collapsed: Boolean): RecordDraftItem {
    return RecordDraftItem(
        lazyListKey = index,
        index = index,
        categoryWithSub = this.categoryWithSub,
        note = this.note ?: "",
        amount = "%.2f".format(
            locale = Locale.US,
            this.amount / (this.quantity.takeUnless { it == 0 } ?: 1)
        ),
        quantity = quantity?.toString() ?: "",
        collapsed = collapsed
    )
}


fun RecordDraft.toCreatedRecord(): CreatedRecord? {
    val account = this.general.account ?: return null
    val items = this.items.toCreatedRecordItems()

    return CreatedRecord(
        isNew = this.general.isNew,
        recordNum = this.general.recordNum,
        account = account,
        type = this.general.type,
        dateLong = this.general.dateTimeState.dateLong,
        preferences = this.general.preferences,
        items = items,
        totalAmount = items.getTotalAmount()
    )
}

private fun List<RecordDraftItem>.toCreatedRecordItems(): List<CreatedRecordItem> {
    return this.mapNotNull(RecordDraftItem::toCreatedRecordItem)
}

private fun RecordDraftItem.toCreatedRecordItem(): CreatedRecordItem? {
    val categoryWithSubcategory = this.categoryWithSub ?: return null

    return CreatedRecordItem(
        categoryWithSub = categoryWithSubcategory,
        note = note.trim().takeIf { it.isNotBlank() },
        totalAmount = this.getTotalAmount(),
        quantity = quantity.toIntOrNull()
    )
}



fun CreatedRecord.toRecordEntities(): List<RecordEntity> {
    return this.items.map { item ->
        item.toRecordEntity(
            recordNum = this.recordNum,
            dateLong = this.dateLong,
            type = this.type.asChar(),
            accountId = this.account.id,
            preferences = this.preferences
        )
    }
}

fun CreatedRecord.toRecordEntitiesWithOldIds(recordStack: RecordStack): List<RecordEntity> {
    return this.items.mapIndexed { index, item ->
        item.toRecordEntity(
            id = recordStack.stack.getOrNull(index)?.id ?: 0,
            recordNum = recordStack.recordNum,
            dateLong = this.dateLong,
            type = this.type.asChar(),
            accountId = this.account.id,
            preferences = this.preferences
        )
    }
}

private fun CreatedRecordItem.toRecordEntity(
    id: Int = 0,
    recordNum: Int,
    dateLong: Long,
    type: Char,
    accountId: Int,
    preferences: RecordDraftPreferences
): RecordEntity {
    return RecordEntity(
        id = id,
        recordNum = recordNum,
        date = dateLong,
        type = type,
        accountId = accountId,
        amount = this.totalAmount,
        quantity = this.quantity,
        categoryId = this.categoryWithSub.category.id,
        subcategoryId = this.categoryWithSub.subcategory?.id,
        note = this.note,
        includeInBudgets = preferences.includeInBudgets
    )
}