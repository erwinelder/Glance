package com.ataglance.walletglance.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CategoryCollection")
data class CategoryCollection(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val orderNum: Int,
    val name: String
)