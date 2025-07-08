package com.ataglance.walletglance.navigation.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "navigation_button")
data class NavigationButtonEntity(
    @PrimaryKey
    val screenName: String,
    val orderNum: Int,
    val timestamp: Long,
    val deleted: Boolean
)