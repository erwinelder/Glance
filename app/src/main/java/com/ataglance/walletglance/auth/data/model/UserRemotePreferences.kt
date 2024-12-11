package com.ataglance.walletglance.auth.data.model

import com.ataglance.walletglance.billing.domain.model.AppSubscription

data class UserRemotePreferences(
    val userId: String,
    val language: String,
    val subscription: AppSubscription
)