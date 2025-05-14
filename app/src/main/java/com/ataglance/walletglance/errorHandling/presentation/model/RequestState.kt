package com.ataglance.walletglance.errorHandling.presentation.model

import androidx.annotation.StringRes

sealed class RequestState {

    data class Loading(@StringRes val messageRes: Int) : RequestState()

    data class Result(val resultState: ResultWithButtonState) : RequestState()

}