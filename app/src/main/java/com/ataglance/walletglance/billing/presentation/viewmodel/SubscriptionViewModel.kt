package com.ataglance.walletglance.billing.presentation.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.ProductDetails
import com.ataglance.walletglance.billing.domain.model.AppSubscriptions
import com.ataglance.walletglance.billing.domain.model.BillingSubscriptionManager
import com.ataglance.walletglance.billing.mapper.toSubscriptionUiState
import com.ataglance.walletglance.billing.mapper.toSubscriptionUiStateList
import com.ataglance.walletglance.billing.presentation.model.SubscriptionUiState
import com.ataglance.walletglance.billing.domain.model.errorHandling.BillingError
import com.ataglance.walletglance.errorHandling.domain.model.result.ResultData
import com.ataglance.walletglance.auth.mapper.toResultState
import com.ataglance.walletglance.errorHandling.presentation.model.ResultTitleWithMessageState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SubscriptionViewModel(
    private val billingSubscriptionManager: BillingSubscriptionManager
) : ViewModel() {

    private val _availableSubscriptions: MutableStateFlow<List<SubscriptionUiState>> =
        MutableStateFlow(emptyList())
    val availableSubscriptions = _availableSubscriptions.asStateFlow()

    private val _activeSubscriptions: MutableStateFlow<List<SubscriptionUiState>> =
        MutableStateFlow(
            billingSubscriptionManager.getProductDetails(AppSubscriptions.Free.id)
                ?.let { listOf(it.toSubscriptionUiState()) }
                .orEmpty()
        )
    val activeSubscriptions = _activeSubscriptions.asStateFlow()

    private suspend fun fetchSubscriptions() {
        val activeSubscriptions = billingSubscriptionManager.activePurchases.firstOrNull().orEmpty()

        val allSubscriptions = billingSubscriptionManager.getProductDetailsList()
        val activeProductIds = activeSubscriptions.map { it.productId }

        val availableSubscriptions = allSubscriptions.filterNot { it.productId in activeProductIds }

        _activeSubscriptions.update { activeSubscriptions.toSubscriptionUiStateList() }
        _availableSubscriptions.update { availableSubscriptions.toSubscriptionUiStateList() }
    }


    private val _purchaseResult: MutableStateFlow<ResultTitleWithMessageState?> = MutableStateFlow(null)
    val purchaseResult = _purchaseResult.asStateFlow()

    private fun observeNewPurchase() {
        viewModelScope.launch {
            billingSubscriptionManager.newPurchase.collect { purchaseResult ->
                val result: ResultData<ProductDetails, BillingError> = when (purchaseResult) {
                    is ResultData.Success -> ResultData.Success(purchaseResult.data)
                    is ResultData.Error -> ResultData.Error(purchaseResult.error)
                }
                _purchaseResult.update {
                    result.toResultState()
                }
            }
        }
    }

    fun resetPurchaseResult() {
        _purchaseResult.update { null }
    }


    init {
        observeNewPurchase()
        viewModelScope.launch {
            fetchSubscriptions()
        }
    }


    fun startPurchase(activity: Activity, subscription: SubscriptionUiState) {
        billingSubscriptionManager.getProductDetails(subscription.id)?.let { productDetails ->
            billingSubscriptionManager.launchBillingFlow(activity, productDetails)
        }
    }

}

class SubscriptionViewModelFactory(
    private val billingSubscriptionManager: BillingSubscriptionManager
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SubscriptionViewModel(billingSubscriptionManager) as T
    }
}