package com.ataglance.walletglance.auth.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CurrentAppVersion(
    val primaryVersion: Int = 5,
    val secondaryVersion: Int = 0,
    val tertiaryVersion: Int = 0,
    val alphaVersion: Int? = 4,
    val betaVersion: Int? = null,
    val releaseCandidateVersion: Int? = null
)