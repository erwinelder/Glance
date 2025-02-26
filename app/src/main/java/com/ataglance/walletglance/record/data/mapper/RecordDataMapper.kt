package com.ataglance.walletglance.record.data.mapper

import com.ataglance.walletglance.core.utils.convertToCharOrNull
import com.ataglance.walletglance.core.utils.convertToDoubleOrZero
import com.ataglance.walletglance.core.utils.convertToIntOrNull
import com.ataglance.walletglance.core.utils.convertToIntOrZero
import com.ataglance.walletglance.record.data.local.model.RecordEntity
import com.ataglance.walletglance.record.data.remote.model.RecordRemoteEntity
import com.ataglance.walletglance.record.domain.model.RecordType
import com.ataglance.walletglance.record.domain.utils.asChar


fun RecordEntity.toRemoteEntity(updateTime: Long, deleted: Boolean): RecordRemoteEntity {
    return RecordRemoteEntity(
        updateTime = updateTime,
        deleted = deleted,
        id = id,
        recordNum = recordNum,
        date = date,
        type = type,
        accountId = accountId,
        amount = amount,
        quantity = quantity,
        categoryId = categoryId,
        subcategoryId = subcategoryId,
        note = note,
        includeInBudgets = includeInBudgets
    )
}

fun RecordRemoteEntity.toLocalEntity(): RecordEntity {
    return RecordEntity(
        id = id,
        recordNum = recordNum,
        date = date,
        type = type,
        accountId = accountId,
        amount = amount,
        quantity = quantity,
        categoryId = categoryId,
        subcategoryId = subcategoryId,
        note = note,
        includeInBudgets = includeInBudgets
    )
}


fun RecordRemoteEntity.toMap(): HashMap<String, Any?> {
    return hashMapOf(
        "updateTime" to updateTime,
        "deleted" to deleted,
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

fun Map<String, Any?>.toRecordRemoteEntity(): RecordRemoteEntity {
    return RecordRemoteEntity(
        updateTime = this["updateTime"] as Long,
        deleted = this["deleted"] as Boolean,
        id = this["id"].convertToIntOrZero(),
        recordNum = this["recordNum"].convertToIntOrZero(),
        date = this["date"] as Long,
        type = this["type"]?.convertToCharOrNull() ?: ' ',
        accountId = this["accountId"].convertToIntOrZero(),
        amount = this["amount"].convertToDoubleOrZero(),
        quantity = this["quantity"]?.convertToIntOrNull(),
        categoryId = this["categoryId"].convertToIntOrZero(),
        subcategoryId = this["subcategoryId"]?.convertToIntOrNull(),
        note = this["note"] as String?,
        includeInBudgets = this["includeInBudgets"] as Boolean
    )
}


fun RecordRemoteEntity.convertTransferToRecord(timestamp: Long): RecordRemoteEntity {
    return copy(
        updateTime = timestamp,
        type = if (isOutTransfer()) RecordType.Expense.asChar() else RecordType.Income.asChar(),
        categoryId = if (isOutTransfer()) 12 else 77,
        subcategoryId = if (isOutTransfer()) 66 else null,
        note = null
    )
}