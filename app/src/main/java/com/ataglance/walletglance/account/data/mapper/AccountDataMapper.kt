package com.ataglance.walletglance.account.data.mapper

import com.ataglance.walletglance.account.data.local.model.AccountEntity
import com.ataglance.walletglance.account.data.remote.model.AccountRemoteEntity
import com.ataglance.walletglance.core.utils.convertToDoubleOrZero
import com.ataglance.walletglance.core.utils.convertToIntOrZero


fun AccountEntity.toRemoteEntity(updateTime: Long, deleted: Boolean): AccountRemoteEntity {
    return AccountRemoteEntity(
        updateTime = updateTime,
        deleted = deleted,
        id = id,
        orderNum = orderNum,
        name = name,
        currency = currency,
        balance = balance,
        color = color,
        hide = hide,
        hideBalance = hideBalance,
        withoutBalance = withoutBalance,
    )
}

fun AccountRemoteEntity.toLocalEntity(): AccountEntity {
    return AccountEntity(
        id = id,
        orderNum = orderNum,
        name = name,
        currency = currency,
        balance = balance,
        color = color,
        hide = hide,
        hideBalance = hideBalance,
        withoutBalance = withoutBalance,
        timestamp = updateTime // TODO-SYNC
    )
}


fun AccountRemoteEntity.toMap(): HashMap<String, Any> {
    return hashMapOf(
        /*"updateTime" to updateTime,
        "deleted" to deleted,
        "id" to id,
        "orderNum" to orderNum,
        "name" to name,
        "currency" to currency,
        "balance" to balance,
        "color" to color,
        "hide" to hide,
        "hideBalance" to hideBalance,
        "withoutBalance" to withoutBalance*/
    )
}

fun Map<String, Any?>.toAccountRemoteEntity(): AccountRemoteEntity {
    return AccountRemoteEntity(
        updateTime = this["updateTime"] as Long,
        deleted = this["deleted"] as Boolean,
        id = this["id"].convertToIntOrZero(),
        orderNum = this["orderNum"].convertToIntOrZero(),
        name = this["name"] as String,
        currency = this["currency"] as String,
        balance = this["balance"].convertToDoubleOrZero(),
        color = this["color"] as String,
        hide = this["hide"] as Boolean,
        hideBalance = this["hideBalance"] as Boolean,
        withoutBalance = this["withoutBalance"] as Boolean
    )
}
