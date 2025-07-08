package com.ataglance.walletglance.auth.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SaveLanguageRequestDto(
    val langCode: String,
    val timestamp: Long
)
