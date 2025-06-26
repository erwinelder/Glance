package com.ataglance.walletglance.personalization.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "widget")
data class WidgetEntity(
    @PrimaryKey
    val name: String,
    val orderNum: Int,
    val timestamp: Long
)
