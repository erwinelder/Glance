package com.ataglance.walletglance.record.data.remote.model

import com.ataglance.walletglance.record.domain.model.RecordType
import com.ataglance.walletglance.record.domain.utils.asChar

data class RecordRemoteEntity(
    val updateTime: Long,
    val deleted: Boolean,
    val id: Int,
    val recordNum: Int,
    val date: Long,
    val type: Char,
    val accountId: Int,
    val amount: Double,
    val quantity: Int?,
    val categoryId: Int,
    val subcategoryId: Int?,
    val note: String?,
    val includeInBudgets: Boolean
) {

    fun isOutTransfer() = type == RecordType.OutTransfer.asChar()

}