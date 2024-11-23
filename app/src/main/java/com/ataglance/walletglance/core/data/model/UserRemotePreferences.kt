package com.ataglance.walletglance.core.data.model

import com.ataglance.walletglance.billing.domain.model.AppSubscription

data class UserRemotePreferences(
    val language: String,
    val subscription: AppSubscription
)
