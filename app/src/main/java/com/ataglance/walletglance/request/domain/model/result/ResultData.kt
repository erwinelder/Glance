package com.ataglance.walletglance.request.domain.model.result

sealed interface ResultData<out D, out E: RootError> {

    data class Success<out D, out E: RootError>(val data: D): ResultData<D, E>
    data class Error<out D, out E: RootError>(val error: E): ResultData<D, E>


    fun getDataIfSuccess(): D? = (this as? Success)?.data
    fun getErrorIfError(): E? = (this as? Error)?.error

    fun <R> mapData(transform: (D) -> R): ResultData<R, E> {
        return when (this) {
            is Success -> Success<R, E>(this.data.let(transform))
            is Error -> Error(this.error)
        }
    }

    fun mapDataToUnit(): ResultData<Unit, E> {
        return when (this) {
            is Success -> Success(Unit)
            is Error -> Error(this.error)
        }
    }

    fun <R : RootError> mapError(transform: (E) -> R): ResultData<D, R> {
        return when (this) {
            is Success -> Success(this.data)
            is Error -> Error<D, R>(this.error.let(transform))
        }
    }

    fun <S : RootSuccess?> toDefaultResult(success: S): Result<S, E> {
        return when (this) {
            is Success -> Result.Success(success)
            is Error -> Result.Error(this.error)
        }
    }

}