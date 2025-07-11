package com.ataglance.walletglance.request.presentation.model

import androidx.annotation.StringRes
import com.ataglance.walletglance.request.presentation.model.ResultState

sealed class RequestState <S : ResultState, E : ResultState> {

    data class Loading<S : ResultState, E : ResultState>(
        @StringRes val messageRes: Int
    ) : RequestState<S, E>()

    data class Success<S : ResultState, E : ResultState>(val state: S): RequestState<S, E>()

    data class Error<S : ResultState, E : ResultState>(val state: E): RequestState<S, E>()

}