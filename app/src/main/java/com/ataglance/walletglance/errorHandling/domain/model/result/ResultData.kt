package com.ataglance.walletglance.errorHandling.domain.model.result

sealed interface ResultData<out D, out E: RootError> {

    data class Success<out D, out E: RootError>(val data: D): ResultData<D, E>
    data class Error<out D, out E: RootError>(val error: E): ResultData<D, E>

    fun getDataIfSuccess(): D? = if (this is Success) this.data else null

}