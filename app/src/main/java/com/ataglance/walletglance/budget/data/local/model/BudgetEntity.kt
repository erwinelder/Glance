package com.ataglance.walletglance.budget.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ataglance.walletglance.category.data.local.model.CategoryEntity

@Entity(
    tableName = "Budget",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["categoryId"])]
)
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val amountLimit: Double,
    val categoryId: Int,
    val name: String,
    val repeatingPeriod: String
)