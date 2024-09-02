package com.ataglance.walletglance.recordCreation.utils

import com.ataglance.walletglance.account.domain.Account
import com.ataglance.walletglance.account.domain.AccountsUiState
import com.ataglance.walletglance.account.utils.findById
import com.ataglance.walletglance.category.domain.CategoryWithSubcategory
import com.ataglance.walletglance.category.utils.asChar
import com.ataglance.walletglance.core.utils.getNewDateByRecordLongDate
import com.ataglance.walletglance.record.data.local.model.RecordEntity
import com.ataglance.walletglance.record.domain.RecordStack
import com.ataglance.walletglance.record.domain.RecordStackItem
import com.ataglance.walletglance.record.utils.findByRecordNum
import com.ataglance.walletglance.record.utils.toCategoryType
import com.ataglance.walletglance.recordCreation.domain.record.CreatedRecord
import com.ataglance.walletglance.recordCreation.domain.record.CreatedRecordItem
import com.ataglance.walletglance.recordCreation.domain.record.RecordDraft
import com.ataglance.walletglance.recordCreation.domain.record.RecordDraftGeneral
import com.ataglance.walletglance.recordCreation.domain.record.RecordDraftItem
import com.ataglance.walletglance.recordCreation.domain.record.RecordDraftPreferences
import java.util.Locale


fun List<RecordStack>.getRecordDraft(
    isNew: Boolean,
    recordNum: Int,
    accountsUiState: AccountsUiState,
    initialCategoryWithSubcategory: CategoryWithSubcategory?
): RecordDraft {
    return this
        .takeUnless { isNew }
        ?.findByRecordNum(recordNum)
        ?.toRecordDraft(accountsUiState.accountList)
        ?: getClearRecordDraft(
            recordNum = recordNum,
            account = accountsUiState.activeAccount,
            categoryWithSubcategory = initialCategoryWithSubcategory
        )
}

private fun getClearRecordDraft(
    recordNum: Int,
    account: Account?,
    categoryWithSubcategory: CategoryWithSubcategory?
): RecordDraft {
    return RecordDraft(
        general = RecordDraftGeneral(
            isNew = true,
            recordNum = recordNum,
            account = account
        ),
        items = listOf(
            RecordDraftItem(
                lazyListKey = 0,
                index = 0,
                categoryWithSubcategory = categoryWithSubcategory
            )
        )
    )
}

private fun RecordStack.toRecordDraft(
    accountList: List<Account>
): RecordDraft? {
    val type = this.type.toCategoryType() ?: return null
    val includeInBudgets = this.stack.firstOrNull()?.includeInBudgets ?: return null

    return RecordDraft(
        general = RecordDraftGeneral(
            isNew = false,
            recordNum = this.recordNum,
            account = accountList.findById(this.account.id),
            type = type,
            dateTimeState = getNewDateByRecordLongDate(this.date),
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
        categoryWithSubcategory = this.categoryWithSubcategory,
        note = this.note ?: "",
        amount = "%.2f".format(
            Locale.US,
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
        dateTimeState = this.general.dateTimeState,
        preferences = this.general.preferences,
        items = items,
        totalAmount = items.getTotalAmount()
    )
}

private fun List<RecordDraftItem>.toCreatedRecordItems(): List<CreatedRecordItem> {
    return this.mapNotNull { it.toCreatedRecordItem() }
}

private fun RecordDraftItem.toCreatedRecordItem(): CreatedRecordItem? {
    val categoryWithSubcategory = this.categoryWithSubcategory ?: return null

    return CreatedRecordItem(
        categoryWithSubcategory = categoryWithSubcategory,
        note = note,
        totalAmount = this.getTotalAmount(),
        quantity = quantity.toIntOrNull()
    )
}


fun CreatedRecord.toRecordEntityList(): List<RecordEntity> {
    return this.items.map { item ->
        item.toRecordEntity(
            recordNum = this.recordNum,
            dateLong = this.dateTimeState.dateLong,
            type = this.type.asChar(),
            accountId = this.account.id,
            preferences = this.preferences
        )
    }
}

fun CreatedRecord.toRecordEntityListWithOldIds(recordStack: RecordStack): List<RecordEntity> {
    return this.items.mapIndexed { index, item ->
        item.toRecordEntity(
            id = recordStack.stack.getOrNull(index)?.id ?: 0,
            recordNum = recordStack.recordNum,
            dateLong = this.dateTimeState.dateLong,
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
        categoryId = this.categoryWithSubcategory.category.id,
        subcategoryId = this.categoryWithSubcategory.subcategory?.id,
        note = this.note,
        includeInBudgets = preferences.includeInBudgets
    )
}



fun List<RecordDraftItem>.copyWithCategoryWithSubcategory(
    categoryWithSubcategory: CategoryWithSubcategory?
): List<RecordDraftItem> {
    return this.map { it.copy(categoryWithSubcategory = categoryWithSubcategory) }
}

fun List<CreatedRecordItem>.getTotalAmount(): Double {
    return this.fold(0.0) { acc, item ->
        acc + item.totalAmount
    }
}
