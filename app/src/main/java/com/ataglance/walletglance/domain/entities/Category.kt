package com.ataglance.walletglance.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ataglance.walletglance.data.categories.CategoryColorName
import com.ataglance.walletglance.data.categories.CategoryType

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
    fun isIncome() = type == '+'

    fun getCategoryType(): CategoryType? {
        return when {
            isExpense() -> CategoryType.Expense
            isIncome() -> CategoryType.Income
            else -> null
        }
    }

    fun isParentCategory() = rank == 'c'
    fun isSubcategory() = rank == 's'

    fun cloneWithNewName(name: String): Category {
        return Category(
            id = id,
            type = type,
            rank = rank,
            orderNum = orderNum,
            parentCategoryId = parentCategoryId,
            name = name,
            iconName = iconName,
            colorName = colorName
        )
    }

}
