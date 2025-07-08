package com.ataglance.walletglance.personalization.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class WidgetDto(
    val name: String,
    val orderNum: Int,
    val timestamp: Long,
    val deleted: Boolean
)
