package com.ataglance.walletglance.errorHandling.domain.model

import androidx.annotation.StringRes

data class TaskResultData<T>(
    val isSuccessful: Boolean,
    @StringRes val messageRes: Int,
    val data: T? = null
) {

    companion object {

        fun <T> success(): TaskResultData<T> {
            return TaskResultData(true, 0)
        }
        fun <T> success(@StringRes messageRes: Int): TaskResultData<T> {
            return TaskResultData(true, messageRes)
        }
        fun <T> success(data: T): TaskResultData<T> {
            return TaskResultData(true, 0, data)
        }
        fun <T> success(@StringRes messageRes: Int, data: T): TaskResultData<T> {
            return TaskResultData(true, messageRes, data)
        }

        fun <T> error(): TaskResultData<T> {
            return TaskResultData(false, 0)
        }
        fun <T> error(@StringRes messageRes: Int): TaskResultData<T> {
            return TaskResultData(false, messageRes)
        }
        fun <T> error(data: T): TaskResultData<T> {
            return TaskResultData(false, 0, data)
        }
        fun <T> error(@StringRes messageRes: Int, data: T): TaskResultData<T> {
            return TaskResultData(false, messageRes, data)
        }

    }

}
