package com.ataglance.walletglance.core.data.model

import com.ataglance.walletglance.auth.domain.AppSubscription

data class UserRemotePreferences(
    val language: String,
    val subscription: AppSubscription
)
