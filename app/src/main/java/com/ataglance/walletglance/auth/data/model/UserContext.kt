package com.ataglance.walletglance.auth.data.model

import com.ataglance.walletglance.billing.domain.model.AppSubscription

class UserContext {

    private var userId = ""

    fun getUserId(): String {
        return userId
    }

    fun setUserId(userId: String) {
        this.userId = userId
    }


    var subscription = AppSubscription.Free

    fun getSubscription(): AppSubscription {
        return subscription
    }

    fun setSubscription(subscription: AppSubscription) {
        this.subscription = subscription
    }

}
