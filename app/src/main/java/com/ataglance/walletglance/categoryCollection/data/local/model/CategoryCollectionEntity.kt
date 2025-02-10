package com.ataglance.walletglance.categoryCollection.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CategoryCollection")
data class CategoryCollectionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val orderNum: Int,
    val type: Char,
    val name: String
)
