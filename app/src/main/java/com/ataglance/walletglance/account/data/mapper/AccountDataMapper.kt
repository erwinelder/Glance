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
        isActive = isActive
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
        isActive = isActive
    )
}


fun AccountRemoteEntity.toMap(): HashMap<String, Any> {
    return hashMapOf(
        "updateTime" to this.updateTime,
        "deleted" to this.deleted,
        "id" to this.id,
        "orderNum" to this.orderNum,
        "name" to this.name,
        "currency" to this.currency,
        "balance" to this.balance,
        "color" to this.color,
        "hide" to this.hide,
        "hideBalance" to this.hideBalance,
        "withoutBalance" to this.withoutBalance,
        "isActive" to this.isActive
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
        withoutBalance = this["withoutBalance"] as Boolean,
        isActive = this["isActive"] as Boolean
    )
}
