package com.ataglance.walletglance.transaction.domain.mapper

import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.core.utils.asList
import com.ataglance.walletglance.transaction.domain.model.Record
import com.ataglance.walletglance.transaction.domain.model.RecordItem
import com.ataglance.walletglance.transaction.domain.model.RecordWithItems
import com.ataglance.walletglance.transaction.domain.model.Transfer


fun Transfer.senderToNewRecord(): Record {
    return Record(
        id = 0,
        date = date,
        type = CategoryType.Expense,
        accountId = senderAccountId,
        includeInBudgets = includeInBudgets
    )
}

fun Transfer.receiverToNewRecord(): Record {
    return Record(
        id = 0,
        date = date,
        type = CategoryType.Income,
        accountId = receiverAccountId,
        includeInBudgets = includeInBudgets
    )
}

fun Transfer.senderToNewRecordItem(): RecordItem {
    return RecordItem(
        id = 0,
        recordId = 0,
        totalAmount = senderAmount,
        quantity = null,
        categoryId = 12,
        subcategoryId = 67,
        note = null
    )
}

fun Transfer.receiverToNewRecordItem(): RecordItem {
    return RecordItem(
        id = 0,
        recordId = 0,
        totalAmount = receiverAmount,
        quantity = null,
        categoryId = 79,
        subcategoryId = null,
        note = null
    )
}

fun Transfer.senderToNewRecordWithItems(): RecordWithItems {
    return RecordWithItems(
        record = senderToNewRecord(),
        items = senderToNewRecordItem().asList()
    )
}

fun Transfer.receiverToNewRecordWithItems(): RecordWithItems {
    return RecordWithItems(
        record = receiverToNewRecord(),
        items = receiverToNewRecordItem().asList()
    )
}
