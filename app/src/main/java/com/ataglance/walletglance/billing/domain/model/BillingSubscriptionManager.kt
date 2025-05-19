package com.ataglance.walletglance.billing.domain.model

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.BillingClient.ProductType
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.Purchase.PurchaseState
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.ataglance.walletglance.auth.domain.model.user.UserContext
import com.ataglance.walletglance.billing.domain.mapper.getProductDetails
import com.ataglance.walletglance.billing.domain.mapper.getProductsDetails
import com.ataglance.walletglance.billing.domain.mapper.subsToProductDetailsParamsList
import com.ataglance.walletglance.billing.domain.usecase.UpdateUserSubscriptionUseCase
import com.ataglance.walletglance.billing.domain.model.errorHandling.BillingError
import com.ataglance.walletglance.errorHandling.domain.model.result.ResultData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlin.math.pow

class BillingSubscriptionManager(
    private val context: Context,
    private val coroutineScope: CoroutineScope,
    private val userContext: UserContext,
    private val updateUserSubscriptionUseCase: UpdateUserSubscriptionUseCase
) {

    private lateinit var billingClient: BillingClient


    private var productDetailsList: List<ProductDetails> = emptyList()

    fun getProductDetailsList(): List<ProductDetails> {
        return productDetailsList
    }

    fun getProductDetails(productId: String): ProductDetails? {
        return productDetailsList.find { it.productId == productId }
    }


    private val _newPurchase: MutableSharedFlow<ResultData<ProductDetails, BillingError>> =
        MutableSharedFlow()
    val newPurchase = _newPurchase.asSharedFlow()

    private val _activePurchases: MutableSharedFlow<List<ProductDetails>> = MutableSharedFlow()
    val activePurchases = _activePurchases.asSharedFlow()


    /** Handle the purchase result.
     */
    private val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
        val result = when (billingResult.responseCode) {
            BillingResponseCode.OK -> handlePurchases(purchases)
            BillingResponseCode.USER_CANCELED -> ResultData.Error(BillingError.UserCancelledPurchase)
            BillingResponseCode.NETWORK_ERROR -> ResultData.Error(BillingError.NoNetwork)
            else -> ResultData.Error(BillingError.Unknown)
        }
        coroutineScope.launch {
            _newPurchase.emit(result)
        }
    }

    private fun handlePurchases(
        purchases: List<Purchase>?
    ): ResultData<ProductDetails, BillingError> {
        val purchase = purchases?.find { it.purchaseState == PurchaseState.PURCHASED }
            ?: return ResultData.Error(BillingError.Unknown)

        val productDetails = purchase.getProductDetails(productDetailsList)
            ?: return ResultData.Error(BillingError.Unknown)

        updateUserSubscription(productDetails).let {
            if (it is ResultData.Error) return ResultData.Error(it.error)
        }

        acknowledgeNewPurchase(purchase).let {
            if (it is ResultData.Error) return ResultData.Error(it.error)
        }

        return ResultData.Success(productDetails)
    }

    private fun updateUserSubscription(
        productDetails: ProductDetails
    ): ResultData<Unit, BillingError> {
        val userId = userContext.getUserIdOld() ?: return ResultData.Error(BillingError.UserNotSignedIn)

        coroutineScope.launch {
            updateUserSubscriptionUseCase.execute(
                userId = userId,
                subscription = productDetails.productId
            )
        }

        return ResultData.Success(Unit)
    }

    private fun acknowledgeNewPurchase(purchase: Purchase): ResultData<Unit, BillingError> {
        if (purchase.isAcknowledged) return ResultData.Success(Unit)

        val params = AcknowledgePurchaseParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()

        var result: ResultData<Unit, BillingError> = ResultData.Success(Unit)
        billingClient.acknowledgePurchase(params) { billingResult ->
            if (billingResult.responseCode == BillingResponseCode.OK) {
                result = ResultData.Success(Unit)
            } else {
                Log.e(
                    "BillingManager",
                    "Failed to acknowledge purchase: ${billingResult.debugMessage}"
                )
                result = ResultData.Error(BillingError.Unknown)
            }
        }

        return result
    }

    /** Launch the subscription purchase process for the user.
     */
    fun launchBillingFlow(activity: Activity, productDetails: ProductDetails) {
        val params = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(
                listOf(
                    BillingFlowParams.ProductDetailsParams.newBuilder()
                        .setProductDetails(productDetails)
                        .build()
                )
            )
            .build()

        billingClient.launchBillingFlow(activity, params)
    }


    init {
        initializeBillingClient()
    }

    /** Set up the billing client and connect it to Google Play.
     */
    private fun initializeBillingClient() {
        billingClient = BillingClient.newBuilder(context)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()

        startConnection()
    }

    /** Start the connection to Google Play.
     */
    private fun startConnection() {
        billingClient.startConnection(object : BillingClientStateListener {

            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingResponseCode.OK) {
                    queryProductsDetails()
                    queryActivePurchases()
                } else {
                    onBillingSetupFailed(billingResult)
                }
            }

            override fun onBillingServiceDisconnected() {
                coroutineScope.launch {
                    retryConnectionWithBackoff()
                }
            }

        })
    }

    private fun onBillingSetupFailed(billingResult: BillingResult) {
        Log.e("BillingManager", "Billing setup failed: ${billingResult.debugMessage}")
    }

    private suspend fun retryConnectionWithBackoff() {
        val retryDelay = 1000L
        val maxRetries = 5
        var retryCount = 0
        var retrySuccess = false

        while (!retrySuccess && retryCount < maxRetries) {
            delay(retryDelay * 2.0.pow(retryCount).toLong())

            billingClient.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode == BillingResponseCode.OK) {
                        retrySuccess = true
                        queryProductsDetails()
                        queryActivePurchases()
                    }
                }

                override fun onBillingServiceDisconnected() {
                    retrySuccess = false
                }
            })

            retryCount++
        }

        if (!retrySuccess) {
            Log.e("BillingManager", "Failed to reconnect to billing service after $maxRetries attempts.")
        }
    }

    /** Query the subscription details (ProductDetails) from Google Play.
     */
    private fun queryProductsDetails() {
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(AppSubscriptions.getPaidSubscriptions().subsToProductDetailsParamsList())
            .build()

        billingClient.queryProductDetailsAsync(params) { billingResult, productDetailsList ->
            if (billingResult.responseCode == BillingResponseCode.OK) {
                this.productDetailsList = productDetailsList
            }
        }
    }

    /** Query the active subscriptions from Google Play.
     */
    private fun queryActivePurchases() {
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(ProductType.SUBS)
            .build()

        billingClient.queryPurchasesAsync(params) { billingResult, purchaseList ->
            if (billingResult.responseCode != BillingResponseCode.OK) return@queryPurchasesAsync

            coroutineScope.launch {
                _activePurchases.emit(
                    purchaseList
                        .filter { it.purchaseState == PurchaseState.PURCHASED }
                        .getProductsDetails(productDetailsList)
                )
            }
        }
    }

}