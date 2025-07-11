package com.ataglance.walletglance.request.domain.model.request

import com.ataglance.walletglance.request.domain.model.result.RootError
import com.ataglance.walletglance.request.domain.model.result.RootSuccess

sealed class RequestState <S : RootSuccess, E : RootError> {

    class Loading<S : RootSuccess, E : RootError> : RequestState<S, E>()

    data class Success<S : RootSuccess, E : RootError>(val result: S): RequestState<S, E>()

    data class Error<S : RootSuccess, E : RootError>(val result: E): RequestState<S, E>()

}