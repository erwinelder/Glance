package com.ataglance.walletglance.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.R
import com.ataglance.walletglance.auth.domain.model.validation.UserDataValidator
import com.ataglance.walletglance.auth.domain.usecase.auth.DeleteAccountUseCase
import com.ataglance.walletglance.auth.mapper.toResultWithButtonState
import com.ataglance.walletglance.auth.mapper.toUiStates
import com.ataglance.walletglance.request.presentation.model.RequestState
import com.ataglance.walletglance.request.presentation.model.ResultState.ButtonState
import com.ataglance.walletglance.request.presentation.model.ValidatedFieldState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DeleteAccountViewModel(
    private val deleteAccountUseCase: DeleteAccountUseCase
) : ViewModel() {

    private val _passwordState = MutableStateFlow(
        ValidatedFieldState(
            validationStates = UserDataValidator.validateRequiredFieldIsNotBlank("").toUiStates()
        )
    )
    val passwordState = _passwordState.asStateFlow()

    fun updateAndValidatePassword(password: String) {
        _passwordState.update {
            it.copy(
                fieldText = password,
                validationStates = UserDataValidator.validateRequiredFieldIsNotBlank(password)
                    .toUiStates()
            )
        }
    }


    val deletionIsAllowed = passwordState.map { passwordState ->
        passwordState.fieldText.isNotBlank()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = false
    )


    private var accountDeletionJob: Job? = null

    fun deleteAccount() {
        if (!deletionIsAllowed.value) return
        setRequestLoadingState()

        accountDeletionJob = viewModelScope.launch {
            val result = deleteAccountUseCase.execute(
                password = passwordState.value.trimmedText
            )
            setRequestResultState(result = result.toResultWithButtonState())
        }
    }

    fun cancelAccountDeletion() {
        accountDeletionJob?.cancel()
        accountDeletionJob = null
        resetRequestState()
    }


    private val _requestState = MutableStateFlow<RequestState<ButtonState>?>(null)
    val requestState = _requestState.asStateFlow()

    private fun setRequestLoadingState() {
        _requestState.update {
            RequestState.Loading(messageRes = R.string.deleting_your_account_loader)
        }
    }

    private fun setRequestResultState(result: ButtonState) {
        _requestState.update { RequestState.Result(resultState = result) }
    }

    fun resetRequestState() {
        _requestState.update { null }
    }

}