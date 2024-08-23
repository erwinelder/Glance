package com.ataglance.walletglance.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ataglance.walletglance.domain.categories.CategoryType
import com.ataglance.walletglance.domain.categories.color.CategoryColorName
import com.ataglance.walletglance.domain.categories.icons.CategoryIcon

@Entity(tableName = "Category")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val type: Char,
    val orderNum: Int,
    val parentCategoryId: Int?,
    val name: String,
    val iconName: String = CategoryIcon.Other.name,
    val colorName: String = CategoryColorName.GrayDefault.name
) {

    fun isExpense() = type == '-'

    fun isParentCategory() = parentCategoryId == null

    fun getCategoryType(): CategoryType? {
        return when (type) {
            '-' -> CategoryType.Expense
            '+' -> CategoryType.Income
            else -> null
        }
    }

}
