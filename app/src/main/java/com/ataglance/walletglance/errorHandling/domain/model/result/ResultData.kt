package com.ataglance.walletglance.errorHandling.domain.model.result

sealed interface ResultData<out D, out E: RootError> {

    data class Success<out D, out E: RootError>(val data: D): ResultData<D, E>
    data class Error<out D, out E: RootError>(val error: E): ResultData<D, E>


    fun getDataIfSuccess(): D? = if (this is Success) this.data else null

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

    fun <S : RootSuccess?> toDefaultResult(success: S): Result<S, E> {
        return when (this) {
            is Success -> Result.Success(success)
            is Error -> Result.Error(this.error)
        }
    }

}