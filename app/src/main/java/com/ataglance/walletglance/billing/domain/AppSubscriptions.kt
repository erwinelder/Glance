package com.ataglance.walletglance.billing.domain

sealed class AppSubscriptions(val id: String, val subscription: AppSubscriptionWithPeriod) {

    data object Free : AppSubscriptions("free", AppSubscriptionWithPeriod.Free)
    data object PremiumMonthly : AppSubscriptions("premium_monthly", AppSubscriptionWithPeriod.PremiumMonthly)
    data object PremiumYearly : AppSubscriptions("premium_yearly", AppSubscriptionWithPeriod.PremiumYearly)

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