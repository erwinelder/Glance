package com.ataglance.walletglance.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ataglance.walletglance.model.CategoryColorName
import com.ataglance.walletglance.model.CategoryType

@Entity(tableName = "Category")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val type: Char,
    val rank: Char,
    val orderNum: Int,
    val parentCategoryId: Int?,
    val name: String,
    val iconName: String,
    val colorName: String = CategoryColorName.GrayDefault.name
) {

    fun isExpense() = type == '-'
    private fun isIncome() = type == '+'

    fun getCategoryType(): CategoryType? {
        return when {
            isExpense() -> CategoryType.Expense
            isIncome() -> CategoryType.Income
            else -> null
        }
    }

    fun isParentCategory() = rank == 'c'

}
