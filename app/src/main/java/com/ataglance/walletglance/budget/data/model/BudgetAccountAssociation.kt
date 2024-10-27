package com.ataglance.walletglance.budget.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.ataglance.walletglance.account.data.model.AccountEntity

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
    ],
    indices = [Index(value = ["accountId"])]
)
data class BudgetAccountAssociation(
    val budgetId: Int,
    val accountId: Int
)
