package com.ataglance.walletglance.domain.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ataglance.walletglance.data.accounts.Account
import com.ataglance.walletglance.data.budgets.Budget
import com.ataglance.walletglance.data.categories.CategoryWithSubcategory
import com.ataglance.walletglance.data.utils.getLongDateRangeWithTime
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
    val amountLimit: Double,
    val categoryId: Int,
    val name: String,
    val repeatingPeriod: String
) {

    fun toBudget(
        categoryWithSubcategory: CategoryWithSubcategory?,
        linkedAccountsIds: List<Int>,
        accountList: List<Account>
    ): Budget? {
        val repeatingPeriodEnum = getRepeatingPeriodByString(repeatingPeriod) ?: return null
        val linkedAccounts = accountList.filter { linkedAccountsIds.contains(it.id) }

        return Budget(
            id = id,
            priorityNum = categoryWithSubcategory?.groupParentAndSubcategoryOrderNums() ?: 0.0,
            usedAmount = 0.0,
            amountLimit = amountLimit,
            usedPercentage = 0F,
            category = categoryWithSubcategory?.getSubcategoryOrCategory(),
            name = name,
            repeatingPeriod = repeatingPeriodEnum,
            dateRange = repeatingPeriodEnum.getLongDateRangeWithTime(),
            currency = linkedAccounts.firstOrNull()?.currency ?: "",
            linkedAccountsIds = linkedAccounts.map { it.id }
        )
    }

}