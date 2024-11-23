package com.ataglance.walletglance.billing.domain.mapper

import com.android.billingclient.api.BillingClient.ProductType
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.QueryProductDetailsParams
import com.ataglance.walletglance.billing.domain.model.AppSubscriptions


fun List<AppSubscriptions>.subsToProductDetailsParamsList(): List<QueryProductDetailsParams.Product> {
    return this.map { subscription ->
        QueryProductDetailsParams.Product.newBuilder()
            .setProductId(subscription.id)
            .setProductType(ProductType.SUBS)
            .build()
    }
}

fun List<ProductDetails>.toProductDetailsParamsList():
        List<BillingFlowParams.ProductDetailsParams> {
    return map { it.toProductDetailsParams() }
}

fun ProductDetails.toProductDetailsParams(): BillingFlowParams.ProductDetailsParams {
    return BillingFlowParams.ProductDetailsParams.newBuilder()
        .setProductDetails(this)
        .build()
}
