package com.ataglance.walletglance.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Category")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val type: Char,
    val rank: Char,
    val orderNum: Int,
    val parentCategoryId: Int?,
    val name: String,
    val iconName: String
)
