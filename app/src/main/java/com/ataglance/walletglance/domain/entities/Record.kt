package com.ataglance.walletglance.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ataglance.walletglance.data.records.RecordStack
import com.ataglance.walletglance.data.records.RecordStackUnit

@Entity(tableName = "Record")
data class Record(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val recordNum: Int,
    val date: Long,
    val type: Char,
    val accountId: Int,
    val amount: Double,
    val quantity: Int?,
    val categoryId: Int,
    val subcategoryId: Int?,
    val note: String?
) {

    fun isOutTransfer(): Boolean { return type == '>' }

    fun toRecordStack(): RecordStack {
        return RecordStack(
            recordNum = recordNum,
            date = date,
            type = type,
            accountId = accountId,
            totalAmount = amount,
            stack = listOf(toRecordStackUnit())
        )
    }

    fun toRecordStackUnit(): RecordStackUnit {
        return RecordStackUnit(
            id = id,
            amount = amount,
            quantity = quantity,
            categoryId = categoryId,
            subcategoryId = subcategoryId,
            note = note
        )
    }
}
