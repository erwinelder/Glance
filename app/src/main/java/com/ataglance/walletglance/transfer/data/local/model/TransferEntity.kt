package com.ataglance.walletglance.transfer.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ataglance.walletglance.account.data.local.model.AccountEntity

@Entity(
    tableName = "transfer",
    foreignKeys = [
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["senderAccountId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["receiverAccountId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["senderAccountId"]),
        Index(value = ["receiverAccountId"]),
    ]
)
data class TransferEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val date: Long,
    val senderAccountId: Int,
    val receiverAccountId: Int,
    val senderAmount: Double,
    val receiverAmount: Double,
    val senderRate: Double,
    val receiverRate: Double,
    val includeInBudgets: Boolean,
    val timestamp: Long,
    val deleted: Boolean
)
