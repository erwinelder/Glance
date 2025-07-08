package com.ataglance.walletglance.budget.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.ataglance.walletglance.account.data.local.model.AccountEntity

@Entity(
    tableName = "budget_account_association",
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
    ],
    indices = [
        Index(value = ["budgetId"]),
        Index(value = ["accountId"])
    ]
)
data class BudgetAccountAssociationEntity(
    val budgetId: Int,
    val accountId: Int
)
