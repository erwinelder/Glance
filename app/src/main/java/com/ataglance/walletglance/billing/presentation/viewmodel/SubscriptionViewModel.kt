package com.ataglance.walletglance.billing.presentation.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.ataglance.walletglance.billing.domain.model.SubscriptionInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SubscriptionViewModel(
//    private val billingManager: BillingManager
) : ViewModel() {

    private val _subscriptionList: MutableStateFlow<List<SubscriptionInfo>> = MutableStateFlow(
        emptyList()
    )
    val subscriptionList = _subscriptionList.asStateFlow()

    fun startPurchase(activity: Activity) {
        /*val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(
                billingManager.productDetailsList.toProductDetailsParamsList()
            )
            .build()

        val billingResult = billingManager.getBillingClient()
            .launchBillingFlow(activity, billingFlowParams)*/
    }

}