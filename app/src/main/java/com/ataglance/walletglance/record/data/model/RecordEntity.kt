package com.ataglance.walletglance.record.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ataglance.walletglance.account.data.local.model.AccountEntity
import com.ataglance.walletglance.record.domain.RecordType
import com.ataglance.walletglance.record.domain.utils.asChar

@Entity(
    tableName = "Record",
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
    val id: Int = 0,
    val recordNum: Int,
    val date: Long,
    val type: Char,
    val accountId: Int,
    val amount: Double,
    val quantity: Int?,
    val categoryId: Int,
    val subcategoryId: Int?,
    val note: String?,
    val includeInBudgets: Boolean
) {

    private fun isExpense() = type == RecordType.Expense.asChar()
    private fun isIncome() = type == RecordType.Income.asChar()
    private fun isOutTransfer() = type == RecordType.OutTransfer.asChar()
    private fun isInTransfer() = type == RecordType.InTransfer.asChar()
    fun isExpenseOrOutTransfer() = isExpense() || isOutTransfer()
    fun isIncomeOrInTransfer() = isIncome() || isInTransfer()

    fun containsParentOrSubcategoryId(id: Int?): Boolean {
        return categoryId == id || subcategoryId == id
    }

    fun convertTransferToRecord(): RecordEntity {
        return copy(
            type = if (isOutTransfer()) RecordType.Expense.asChar() else RecordType.Income.asChar(),
            categoryId = if (isOutTransfer()) 12 else 77,
            subcategoryId = if (isOutTransfer()) 66 else null,
            note = null
        )
    }

}
