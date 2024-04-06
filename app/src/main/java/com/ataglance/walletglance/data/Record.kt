package com.ataglance.walletglance.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ataglance.walletglance.model.RecordStackUnit

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

    fun toRecordStackUnit(): RecordStackUnit {
        return RecordStackUnit(
            id = this.id,
            amount = this.amount,
            quantity = this.quantity,
            categoryId = this.categoryId,
            subcategoryId = this.subcategoryId,
            note = this.note
        )
    }
}
