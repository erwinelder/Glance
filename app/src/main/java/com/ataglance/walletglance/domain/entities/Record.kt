package com.ataglance.walletglance.domain.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.ataglance.walletglance.data.accounts.Account
import com.ataglance.walletglance.data.categories.CategoriesWithSubcategories
import com.ataglance.walletglance.data.categories.CategoryWithSubcategory
import com.ataglance.walletglance.data.records.RecordStack
import com.ataglance.walletglance.data.records.RecordStackUnit
import com.ataglance.walletglance.data.utils.findById
import com.ataglance.walletglance.data.utils.getRecordTypeByChar
import com.ataglance.walletglance.data.utils.toCategoryType

@Entity(
    tableName = "Record",
    foreignKeys = [
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["accountId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Record(
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
    val note: String?
) {

    fun toRecordStack(
        accountList: List<Account>,
        categoriesWithSubcategories: CategoriesWithSubcategories
    ): RecordStack? {
        val recordType = getRecordTypeByChar(type) ?: return null
        val recordAccount = accountList.findById(accountId)?.toRecordAccount() ?: return null
        val recordStackUnit = toRecordStackUnit(categoriesWithSubcategories) ?: return null

        return RecordStack(
            recordNum = recordNum,
            date = date,
            type = recordType,
            account = recordAccount,
            totalAmount = amount,
            stack = listOf(recordStackUnit)
        )
    }

    fun toRecordStackUnit(
        categoriesWithSubcategories: CategoriesWithSubcategories
    ): RecordStackUnit? {
        val recordType = getRecordTypeByChar(type) ?: return null

        val categoryWithSubcategories = recordType.toCategoryType()?.let {
            categoriesWithSubcategories.findById(categoryId, it)
        }

        return RecordStackUnit(
            id = id,
            amount = amount,
            quantity = quantity,
            categoryWithSubcategory = subcategoryId?.let {
                categoryWithSubcategories?.getWithSubcategoryWithId(it)
            } ?: categoryWithSubcategories?.category?.let { CategoryWithSubcategory(it) },
            note = note
        )
    }
}
