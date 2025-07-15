package com.ataglance.walletglance.auth.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CurrentAppVersion(
    val primaryVersion: Int,
    val secondaryVersion: Int,
    val tertiaryVersion: Int,
    val alphaVersion: Int?,
    val betaVersion: Int?,
    val releaseCandidateVersion: Int?
)