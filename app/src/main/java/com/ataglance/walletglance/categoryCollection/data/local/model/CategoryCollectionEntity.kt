package com.ataglance.walletglance.categoryCollection.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category_collection")
data class CategoryCollectionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val orderNum: Int,
    val type: Char,
    val name: String,
    val timestamp: Long,
    val deleted: Boolean
)
