package com.ataglance.walletglance.billing.domain

import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.ataglance.walletglance.billing.domain.mapper.toProductDetailsParamsList

class BillingManager(private val context: Context) {

    private lateinit var billingClient: BillingClient
    var productDetailsList: List<ProductDetails> = emptyList()

    fun getBillingClient(): BillingClient {
        return billingClient
    }

    init {
        initializeBillingClient()
    }

    private fun initializeBillingClient() {
        billingClient = BillingClient.newBuilder(context)
            .setListener(purchasesUpdatedListener)
            .build()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingResponseCode.OK) {
                    onBillingSetupSucceed()
                } else {
                    onBillingSetupFailed()
                }
            }

            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        })
    }

    private val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
        if (billingResult.responseCode == BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                // handle purchase
            }
        } else if (billingResult.responseCode == BillingResponseCode.USER_CANCELED) {
            // handle an error caused by a user cancelling the purchase flow
        } else {
            // handle any other error
        }
    }

    private fun onBillingSetupSucceed() {
        val queryProductDetailsParams = QueryProductDetailsParams.newBuilder()
            .setProductList(AppSubscriptions.asPaidSubscriptionsList().toProductDetailsParamsList())
            .build()

        billingClient.queryProductDetailsAsync(
            queryProductDetailsParams
        ) { billingResult, productDetailsList ->

            if (billingResult.responseCode == BillingResponseCode.OK) {
                this.productDetailsList = productDetailsList
            }

        }
    }

    private fun onBillingSetupFailed() {
        // handle error
    }

}