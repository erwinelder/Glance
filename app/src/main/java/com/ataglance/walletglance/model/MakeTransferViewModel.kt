package com.ataglance.walletglance.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ataglance.walletglance.data.Account
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Locale

class MakeTransferViewModel(
    private val accountList: List<Account>,
    makeTransferUiState: MakeTransferUiState
) : ViewModel() {

    private val _uiState: MutableStateFlow<MakeTransferUiState> =
        MutableStateFlow(makeTransferUiState)
    val uiState: StateFlow<MakeTransferUiState> = _uiState.asStateFlow()

    fun selectNewDate(selectedDateMillis: Long) {
        _uiState.update { it.copy(
            dateTimeState = DateTimeController().getNewDate(
                selectedDateMillis = selectedDateMillis,
                dateTimeState = uiState.value.dateTimeState
            )
        ) }
    }

    fun selectNewTime(hour: Int, minute: Int) {
        _uiState.update { it.copy(
            dateTimeState = DateTimeController().getNewTime(
                dateTimeState = uiState.value.dateTimeState,
                hour = hour,
                minute = minute
            )
        ) }
    }

    fun chooseFromAccount(account: Account) {
        if (uiState.value.toAccount != account) {
            _uiState.update { it.copy(fromAccount = account) }
        } else {
            _uiState.update { it.copy(
                fromAccount = account,
                toAccount = AccountController().getAccountAnotherFrom(account, accountList)
            ) }
        }
    }

    fun chooseToAccount(account: Account) {
        if (uiState.value.fromAccount != account) {
            _uiState.update { it.copy(toAccount = account) }
        } else {
            _uiState.update { it.copy(
                fromAccount = AccountController().getAccountAnotherFrom(account, accountList),
                toAccount = account
            ) }
        }
    }

    fun changeStartRate(value: String) {
        val newValue = value.takeIf {
            Regex("^(?:[1-9]\\d{0,9}(?:[.,]\\d{0,2})?)?\$").matches(it)
        } ?: return
        _uiState.update { it.copy(
            startRate = newValue,
            finalAmount = getAppropriateFinalAmount(startRate = newValue)
        ) }
    }

    fun changeFinalRate(value: String) {
        val newValue = value.takeIf {
            Regex("^(?:[1-9]\\d{0,9}(?:[.,]\\d{0,2})?)?\$").matches(it)
        } ?: return
        _uiState.update { it.copy(
            finalRate = newValue,
            finalAmount = getAppropriateFinalAmount(finalRate = newValue)
        ) }
    }

    fun changeStartAmount(value: String) {
        val newValue = value.takeIf {
            Regex("^(?:[1-9]\\d{0,9}(?:[.,]\\d{0,2})?)?\$").matches(it)
        } ?: return
        _uiState.update { it.copy(
            startAmount = newValue,
            finalAmount = getAppropriateFinalAmount(startAmount = newValue)
        ) }
    }

    fun changeFinalAmount(value: String) {
        val newValue = value.takeIf {
            Regex("^(?:[1-9]\\d{0,9}(?:[.,]\\d{0,2})?)?\$").matches(it)
        } ?: return
        _uiState.update { it.copy(
            finalAmount = newValue,
            finalRate = getAppropriateFinalRate(newValue)
        ) }
    }

    private fun getAppropriateFinalAmount(
        startRate: String = uiState.value.startRate,
        finalRate: String = uiState.value.finalRate,
        startAmount: String = uiState.value.startAmount
    ): String {
        return if (startRate.isBlank() || finalRate.isBlank() || startAmount.isBlank()) {
            uiState.value.finalAmount
        } else {
            "%.2f".format(
                Locale.US,
                startAmount.toDouble() / startRate.toDouble() * finalRate.toDouble()
            )
        }
    }

    private fun getAppropriateFinalRate(finalAmount: String): String {
        return if (
            finalAmount.isBlank() ||
            uiState.value.startRate.isBlank() ||
            uiState.value.startAmount.isBlank()
        ) {
            uiState.value.finalRate
        } else {
            "%.2f".format(
                Locale.US,
                finalAmount.toDouble() *
                uiState.value.startRate.toDouble() /
                uiState.value.startAmount.toDouble()
            )
        }
    }

}

class MakeTransferViewModelFactory(
    private val accountList: List<Account>,
    private val makeTransferUiState: MakeTransferUiState
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MakeTransferViewModel(accountList, makeTransferUiState) as T
    }
}

data class MakeTransferUiState(
    val recordStatus: MakeRecordStatus,
    val fromAccount: Account?,
    val toAccount: Account?,
    val startAmount: String = "",
    val startRate: String = "1",
    val finalAmount: String = "",
    val finalRate: String = "1",
    val dateTimeState: DateTimeState = DateTimeState(),
    val recordNum: Int? = null,
    val idFrom: Int = 0,
    val idTo: Int = 0
)