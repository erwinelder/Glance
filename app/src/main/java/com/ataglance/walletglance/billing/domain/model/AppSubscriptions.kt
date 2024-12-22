package com.ataglance.walletglance.billing.domain.model

import com.ataglance.walletglance.R

sealed class AppSubscriptions(
    val id: String,
    val benefits: List<Int>,
    val subscription: AppSubscriptionBasePlan
) {

    val possibleBenefits: List<Int> = listOf(
        R.string.all_basic_app_features,
        R.string.cloud_data_sync
    )

    data object Free : AppSubscriptions(
        id = "free",
        benefits = listOf(
            R.string.all_basic_app_features
        ),
        subscription = AppSubscriptionBasePlan.Free
    )
    data object PlusMonthly : AppSubscriptions(
        id = "plus-monthly",
        benefits = listOf(
            R.string.all_basic_app_features,
            R.string.cloud_data_sync
        ),
        subscription = AppSubscriptionBasePlan.PlusMonthly
    )
    data object PlusMonthlyFreeTrial : AppSubscriptions(
        id = "plus-monthly-free-trial",
        benefits = listOf(
            R.string.all_basic_app_features,
            R.string.cloud_data_sync
        ),
        subscription = AppSubscriptionBasePlan.PlusMonthlyFreeTrial
    )
    data object PlusYearly : AppSubscriptions(
        id = "plus-yearly",
        benefits = listOf(
            R.string.all_basic_app_features,
            R.string.cloud_data_sync
        ),
        subscription = AppSubscriptionBasePlan.PlusYearly
    )
    data object PlusYearlyFreeTrial : AppSubscriptions(
        id = "plus-yearly-free-trial",
        benefits = listOf(
            R.string.all_basic_app_features,
            R.string.cloud_data_sync
        ),
        subscription = AppSubscriptionBasePlan.PlusYearlyFreeTrial
    )

    companion object {

        fun getPaidSubscriptions(): List<AppSubscriptions> {
            return listOf(PlusMonthly, PlusYearly)
        }

        fun fromString(id: String): AppSubscriptions? {
            return when (id) {
                Free.id -> Free
                PlusMonthly.id -> PlusMonthly
                PlusMonthlyFreeTrial.id -> PlusMonthlyFreeTrial
                PlusYearly.id -> PlusYearly
                else -> null
            }
        }

    }

}