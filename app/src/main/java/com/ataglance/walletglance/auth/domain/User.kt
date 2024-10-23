package com.ataglance.walletglance.auth.domain

data class User(
    val uid: String? = null,
    val subscription: AppSubscription = AppSubscription.Free
) {

    fun isSignedIn(): Boolean = uid != null

    fun isAliableForDataSync(): Boolean {
        return isSignedIn() && subscription != AppSubscription.Free
    }

}
