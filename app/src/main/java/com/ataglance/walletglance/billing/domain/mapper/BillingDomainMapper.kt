package com.ataglance.walletglance.billing.domain.mapper

import com.android.billingclient.api.BillingClient.ProductType
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.QueryProductDetailsParams
import com.ataglance.walletglance.billing.domain.model.AppSubscription
import com.ataglance.walletglance.billing.domain.model.AppSubscriptionWithPeriod
import com.ataglance.walletglance.billing.domain.model.AppSubscriptions


fun List<AppSubscriptions>.subsToProductDetailsParamsList(): List<QueryProductDetailsParams.Product> {
    return this.map { subscription ->
        QueryProductDetailsParams.Product.newBuilder()
            .setProductId(subscription.id)
            .setProductType(ProductType.SUBS)
            .build()
    }
}


fun Purchase.getProductDetails(allProductsDetailsList: List<ProductDetails>): ProductDetails? {
    return allProductsDetailsList.find { it.productId in this.products }
}

fun List<Purchase>.getProductsDetails(
    allProductsDetailsList: List<ProductDetails>
): List<ProductDetails> {
    return mapNotNull { it.getProductDetails(allProductsDetailsList) }
}


fun AppSubscriptionWithPeriod.asAppSubscription(): AppSubscription {
    return when (this) {
        AppSubscriptionWithPeriod.Free -> AppSubscription.Free
        AppSubscriptionWithPeriod.PremiumMonthly -> AppSubscription.Premium
        AppSubscriptionWithPeriod.PremiumYearly -> AppSubscription.Premium
    }
}

fun ProductDetails.asAppSubscription(): AppSubscription? {
    return AppSubscriptions.fromString(this.productId)?.subscription?.asAppSubscription()
}