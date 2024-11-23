package com.ataglance.walletglance.auth.domain.model

import com.ataglance.walletglance.billing.domain.model.AppSubscription

data class User(
    val uid: String? = null,
    val subscription: AppSubscription = AppSubscription.Free
) {

    fun isSignedIn(): Boolean = uid != null

    fun isEligibleForDataSync(): Boolean {
        return isSignedIn() && subscription != AppSubscription.Free
    }

}
