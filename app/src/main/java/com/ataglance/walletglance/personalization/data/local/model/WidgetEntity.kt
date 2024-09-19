package com.ataglance.walletglance.personalization.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Widget")
data class WidgetEntity(
    @PrimaryKey(autoGenerate = false)
    val name: String,
    val orderNum: Int
)
