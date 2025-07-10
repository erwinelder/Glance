package com.ataglance.walletglance.request.presentation.model

import androidx.annotation.StringRes

sealed class RequestState <T : ResultState> {

    data class Loading<T : ResultState>(@StringRes val messageRes: Int) : RequestState<T>()

    data class Result<T : ResultState>(val resultState: T): RequestState<T>()

}