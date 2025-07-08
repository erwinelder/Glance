package com.ataglance.walletglance.navigation.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class NavigationButtonDto(
    val screenName: String,
    val orderNum: Int,
    val timestamp: Long,
    val deleted: Boolean
)