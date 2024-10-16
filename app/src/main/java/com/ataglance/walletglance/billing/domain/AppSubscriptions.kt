package com.ataglance.walletglance.billing.domain

sealed class AppSubscriptions(val id: String, val subscription: AppSubscription) {

    data object Free : AppSubscriptions("free", AppSubscription.Free)
    data object PremiumMonthly : AppSubscriptions("premium_monthly", AppSubscription.PremiumMonthly)
    data object PremiumYearly : AppSubscriptions("premium_yearly", AppSubscription.PremiumYearly)

    companion object {

        fun asPaidSubscriptionsList(): List<AppSubscriptions> {
            return listOf(PremiumMonthly, PremiumYearly)
        }

        fun fromString(id: String): AppSubscriptions? {
            return when (id) {
                Free.id -> Free
                PremiumMonthly.id -> PremiumMonthly
                PremiumYearly.id -> PremiumYearly
                else -> null
            }
        }

    }

}