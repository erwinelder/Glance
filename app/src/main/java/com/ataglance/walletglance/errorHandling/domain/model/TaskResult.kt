package com.ataglance.walletglance.errorHandling.domain.model

import androidx.annotation.StringRes

data class TaskResult(
    val isSuccessful: Boolean,
    @StringRes val messageRes: Int
) {

    companion object {

        fun success(): TaskResult {
            return TaskResult(true, 0)
        }
        fun success(@StringRes messageRes: Int): TaskResult {
            return TaskResult(true, messageRes)
        }

        fun error(): TaskResult {
            return TaskResult(false, 0)
        }
        fun error(@StringRes messageRes: Int): TaskResult {
            return TaskResult(false, messageRes)
        }

    }

}
