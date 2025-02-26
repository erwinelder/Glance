package com.ataglance.walletglance.errorHandling.domain.model.validation

sealed class ValidationResult<out V>(val validation: V) {

    data class Success<out V: Validation>(val v: V): ValidationResult<V>(v)
    data class Error<out V: Validation>(val v: V): ValidationResult<V>(v)

    companion object {
        fun <V: Validation> fromValidation(validation: V, isValid: Boolean): ValidationResult<V> {
            return if (isValid) Success(validation) else Error(validation)
        }
    }

}