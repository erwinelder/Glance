package com.ataglance.walletglance.request.domain.model.validation

sealed class ValidationResult<out V : Validation>(val validation: V) {

    data class Success<out V: Validation>(val v: V): ValidationResult<V>(v)
    data class Error<out V: Validation>(val v: V): ValidationResult<V>(v)

    fun takeIfSuccess(): Success<V>? = this as? Success<V>
    fun takeIfError(): Error<V>? = this as? Error<V>

    companion object {

        fun <V: Validation> fromValidation(validation: V, isValid: Boolean): ValidationResult<V> {
            return if (isValid) Success(validation) else Error(validation)
        }

        fun <V : Validation> fromValidationIfSuccess(validation: V, isValid: Boolean): Success<V>? {
            return if (isValid) Success(validation) else null
        }

        fun <V : Validation> fromValidationIfError(validation: V, isValid: Boolean): Error<V>? {
            return if (!isValid) Error(validation) else null
        }

    }

}