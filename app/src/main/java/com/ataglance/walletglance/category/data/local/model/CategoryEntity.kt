package com.ataglance.walletglance.category.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val type: Char,
    val orderNum: Int,
    val parentCategoryId: Int?,
    val name: String,
    val iconName: String,
    val colorName: String,
    val timestamp: Long,
    val deleted: Boolean
)
