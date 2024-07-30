package com.ataglance.walletglance.domain.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ataglance.walletglance.data.budgets.Budget
import com.ataglance.walletglance.data.categories.Category
import com.ataglance.walletglance.data.utils.getRepeatingPeriodByString

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
    val usedAmount: Double,
    val amountLimit: Double,
    val categoryId: Int,
    val name: String,
    val repeatingPeriod: String,
    val lastResetDate: Long,
) {

    fun toBudget(category: Category?, budgetAccountsIds: List<Int>): Budget? {
        val repeatingPeriodEnum = getRepeatingPeriodByString(repeatingPeriod) ?: return null

        return Budget(
            id = id,
            usedAmount = usedAmount,
            amountLimit = amountLimit,
            usedPercentage = (100 / amountLimit * usedAmount).toFloat(),
            category = category,
            name = name,
            repeatingPeriod = repeatingPeriodEnum,
            lastResetDate = lastResetDate,
            linkedAccountsIds = budgetAccountsIds
        )
    }

}