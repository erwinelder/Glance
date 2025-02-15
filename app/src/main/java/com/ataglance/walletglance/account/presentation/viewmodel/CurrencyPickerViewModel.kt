package com.ataglance.walletglance.account.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.account.domain.utils.toSortedCurrencyItemList
import com.ataglance.walletglance.account.presentation.model.CurrencyItem
import com.ataglance.walletglance.account.presentation.model.CurrencyPickerUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.util.Currency

class CurrencyPickerViewModel(selectedCurrency: String?) : ViewModel() {

    private val _searchedPrompt = MutableStateFlow("")
    val searchedPrompt = _searchedPrompt.asStateFlow()

    private val _currencyList: MutableStateFlow<List<CurrencyItem>> = MutableStateFlow(
        Currency.getAvailableCurrencies().toSortedCurrencyItemList()
    )

    val currencyList: StateFlow<List<CurrencyItem>> = searchedPrompt.combine(
        _currencyList
    ) { prompt, list ->
        if (prompt.isBlank()) {
            list
        } else {
            list.filter { it.doesMatchSearchQuery(prompt) }
        }
    }
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = _currencyList.value
    )

    private val _uiState: MutableStateFlow<CurrencyPickerUiState> = MutableStateFlow(
        selectedCurrency?.let {
            try {
                CurrencyPickerUiState(selectedCurrency = CurrencyItem(Currency.getInstance(it)))
            } catch (e: Exception) {
                Log.e(
                    e.toString(),
                    "Error in CurrencyPickerViewModel: passed currency code is not valid"
                )
                null
            }
        } ?: CurrencyPickerUiState(selectedCurrency = currencyList.value.firstOrNull())
    )
    val uiState = _uiState.asStateFlow()

    fun selectCurrency(currency: CurrencyItem) {
        _uiState.update { it.copy(selectedCurrency = currency) }
    }

    fun changeSearchPrompt(prompt: String) {
        _searchedPrompt.update { prompt }
        _uiState.update { it.copy(isSearching = prompt.isNotEmpty()) }
    }

}

data class CurrencyPickerViewModelFactory(
    private val selectedCurrency: String?
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CurrencyPickerViewModel(selectedCurrency) as T
    }
}