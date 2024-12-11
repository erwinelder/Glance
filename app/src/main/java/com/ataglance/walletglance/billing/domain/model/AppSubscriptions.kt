package com.ataglance.walletglance.billing.domain.model

sealed class AppSubscriptions(val id: String, val subscription: AppSubscriptionWithPeriod) {

    data object Free : AppSubscriptions(
        id = "free",
        subscription = AppSubscriptionWithPeriod.Free
    )
    data object PremiumMonthly : AppSubscriptions(
        id = "premium_monthly",
        subscription = AppSubscriptionWithPeriod.PremiumMonthly
    )
    data object PremiumYearly : AppSubscriptions(
        id = "premium_yearly",
        subscription = AppSubscriptionWithPeriod.PremiumYearly
    )

    companion object {

        fun getPaidSubscriptions(): List<AppSubscriptions> {
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