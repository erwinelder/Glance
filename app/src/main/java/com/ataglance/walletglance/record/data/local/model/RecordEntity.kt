package com.ataglance.walletglance.record.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ataglance.walletglance.account.data.local.model.AccountEntity

@Entity(
    tableName = "record",
    foreignKeys = [
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["accountId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["accountId"])]
)
data class RecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val date: Long,
    val type: Char,
    val accountId: Int,
    val includeInBudgets: Boolean,
    val timestamp: Long,
    val deleted: Boolean
)
