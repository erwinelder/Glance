package com.ataglance.walletglance.navigation.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "NavigationButton")
data class NavigationButtonEntity(
    @PrimaryKey
    val screenName: String,
    val orderNum: Int
)