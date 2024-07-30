package com.ataglance.walletglance.domain.entities

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "BudgetAccountAssociation",
    primaryKeys = ["budgetId", "accountId"],
    foreignKeys = [
        ForeignKey(
            entity = BudgetEntity::class,
            parentColumns = ["id"],
            childColumns = ["budgetId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["accountId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class BudgetAccountAssociation(
    val budgetId: Int,
    val accountId: Int
)
