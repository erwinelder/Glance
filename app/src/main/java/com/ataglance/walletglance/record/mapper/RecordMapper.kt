package com.ataglance.walletglance.record.mapper

import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.category.domain.model.GroupedCategoriesByType
import com.ataglance.walletglance.core.domain.date.DateTimeState
import com.ataglance.walletglance.record.data.model.RecordDataModel
import com.ataglance.walletglance.record.data.model.RecordDataModelWithItems
import com.ataglance.walletglance.record.data.model.RecordItemDataModel
import com.ataglance.walletglance.record.presentation.model.RecordDraft
import com.ataglance.walletglance.record.presentation.model.RecordDraftItem
import com.ataglance.walletglance.record.presentation.model.RecordDraftPreferences
import com.ataglance.walletglance.record.presentation.model.RecordDraftWithItems
import com.ataglance.walletglance.transaction.domain.model.Record
import com.ataglance.walletglance.transaction.domain.model.RecordItem
import com.ataglance.walletglance.transaction.domain.model.RecordWithItems
import java.util.Locale


fun RecordDataModel.toDomainModel(): Record? {
    val type = CategoryType.fromChar(char = type) ?: return null

    return Record(
        id = id,
        date = date,
        type = type,
        accountId = accountId,
        includeInBudgets = includeInBudgets
    )
}

fun RecordItemDataModel.toDomainModel(): RecordItem {
    return RecordItem(
        id = id,
        recordId = recordId,
        totalAmount = totalAmount,
        quantity = quantity,
        categoryId = categoryId,
        subcategoryId = subcategoryId,
        note = note
    )
}

fun RecordDataModelWithItems.toDomainModelWithItems(): RecordWithItems? {
    val record = record.toDomainModel() ?: return null
    val items = items.map { it.toDomainModel() }

    return RecordWithItems(record = record, items = items)
}


fun Record.toDataModel(): RecordDataModel {
    return RecordDataModel(
        id = id,
        date = date,
        type = type.asChar(),
        accountId = accountId,
        includeInBudgets = includeInBudgets
    )
}

fun RecordItem.toDataModel(): RecordItemDataModel {
    return RecordItemDataModel(
        id = id,
        recordId = recordId,
        totalAmount = totalAmount,
        quantity = quantity,
        categoryId = categoryId,
        subcategoryId = subcategoryId,
        note = note
    )
}

fun RecordWithItems.toDataModelWithItems(): RecordDataModelWithItems {
    return RecordDataModelWithItems(
        record = record.toDataModel(),
        items = items.map { it.toDataModel() }
    )
}


fun Record.toDraft(accounts: List<Account>): RecordDraft {
    val account = accounts.find { it.id == accountId }

    return RecordDraft(
        recordId = id,
        type = type,
        dateTimeState = DateTimeState.fromTimestamp(timestamp = date),
        account = account,
        preferences = RecordDraftPreferences(
            includeInBudgets = includeInBudgets
        )
    )
}

fun RecordItem.toDraft(
    categories: GroupedCategoriesByType,
    type: CategoryType,
    index: Int,
    collapsed: Boolean
): RecordDraftItem {
    val categoryWithSub = categories.getCategoryWithSub(
        categoryId = categoryId,
        subcategoryId = subcategoryId,
        type = type
    )

    return RecordDraftItem(
        id = id,
        lazyListKey = index,
        categoryWithSub = categoryWithSub,
        note = note ?: "",
        amount = "%.2f".format(
            locale = Locale.US,
            totalAmount / (quantity.takeUnless { it == 0 } ?: 1)
        ),
        quantity = quantity?.toString() ?: "",
        collapsed = collapsed
    )
}

fun RecordWithItems.toDraftWithItems(
    accounts: List<Account>,
    categories: GroupedCategoriesByType
): RecordDraftWithItems? {
    val record = record.toDraft(accounts = accounts)
    val collapsed = items.size != 1
    val items = items.mapIndexed { index, item ->
        item.toDraft(
            categories = categories, type = record.type, index = index, collapsed = collapsed
        )
    }

    return RecordDraftWithItems(record = record, items = items)
}


fun RecordDraft.toDomainModel(): Record? {
    val accountId = account?.id ?: return null

    return Record(
        id = recordId,
        date = dateTimeState.timestamp,
        type = type,
        accountId = accountId,
        includeInBudgets = preferences.includeInBudgets
    )
}

fun RecordDraftItem.toDomainModel(recordId: Long): RecordItem? {
    val categoryWithSub = categoryWithSub ?: return null

    return RecordItem(
        id = id,
        recordId = recordId,
        totalAmount = getTotalAmount(),
        quantity = quantity.toIntOrNull(),
        categoryId = categoryWithSub.categoryId,
        subcategoryId = categoryWithSub.subcategoryId,
        note = note.trim()
    )
}

fun RecordDraftWithItems.toDomainModelWithItems(): RecordWithItems? {
    val record = record.toDomainModel() ?: return null
    val items = items.mapNotNull { it.toDomainModel(recordId = record.id) }

    return RecordWithItems(record = record, items = items)
}
