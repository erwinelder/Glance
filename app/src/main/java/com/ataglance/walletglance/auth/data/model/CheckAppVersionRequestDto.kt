package com.ataglance.walletglance.auth.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CheckAppVersionRequestDto(
    val primaryVersion: Int,
    val secondaryVersion: Int,
    val tertiaryVersion: Int
)