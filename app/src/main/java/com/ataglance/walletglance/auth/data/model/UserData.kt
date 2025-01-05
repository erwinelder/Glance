package com.ataglance.walletglance.auth.data.model

import com.ataglance.walletglance.billing.domain.model.AppSubscription

data class UserData(
    val userId: String,
    val language: String,
    val subscription: AppSubscription
)