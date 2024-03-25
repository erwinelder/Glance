package com.ataglance.walletglance.data

import androidx.room.Entity
import androidx.room.PrimaryKey

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
)
