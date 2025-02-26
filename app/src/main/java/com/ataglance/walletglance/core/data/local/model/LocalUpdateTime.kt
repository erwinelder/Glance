package com.ataglance.walletglance.core.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "local_update_time")
data class LocalUpdateTime(
    @PrimaryKey
    val tableName: String,
    val timestamp: Long
)
