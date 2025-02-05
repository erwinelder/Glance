package com.ataglance.walletglance.personalization.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ataglance.walletglance.budget.data.local.model.BudgetEntity

@Entity(
    tableName = "BudgetOnWidget",
    foreignKeys = [
        ForeignKey(
            entity = BudgetEntity::class,
            parentColumns = ["id"],
            childColumns = ["budgetId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["budgetId"])]
)
data class BudgetOnWidgetEntity(
    @PrimaryKey(autoGenerate = false)
    val budgetId: Int
)
