package com.ataglance.walletglance.request.presentation.model

import androidx.annotation.StringRes
import com.ataglance.walletglance.request.presentation.model.ResultState

sealed class RequestErrorState <E : ResultState> {

    data class Loading<E : ResultState>(@StringRes val messageRes: Int) : RequestErrorState<E>()

    data class Error<E : ResultState>(val state: E) : RequestErrorState<E>()

}