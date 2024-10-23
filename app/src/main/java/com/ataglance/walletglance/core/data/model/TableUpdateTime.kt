package com.ataglance.walletglance.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TableUpdateTime")
data class TableUpdateTime(
    @PrimaryKey
    val tableName: String,
    val timestamp: Long
)
