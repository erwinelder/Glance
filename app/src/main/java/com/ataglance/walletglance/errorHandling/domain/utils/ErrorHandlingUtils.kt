package com.ataglance.walletglance.errorHandling.domain.utils

import androidx.annotation.StringRes
import com.ataglance.walletglance.errorHandling.domain.model.TaskResult

inline fun getTaskResultRunning(
    @StringRes successMessageRes: Int = 0,
    @StringRes errorMessageRes: Int = 0,
    block: () -> Unit
): TaskResult {
    val result = runCatching { block() }

    return if (result.isSuccess) {
        TaskResult.success(messageRes = successMessageRes)
    } else {
        TaskResult.error(messageRes = errorMessageRes)
    }
}

inline fun getTaskErrorRunning(
    @StringRes errorMessageRes: Int = 0,
    block: () -> Unit
): TaskResult? {
    return runCatching { block() }
        .exceptionOrNull()
        ?.let { TaskResult.error(messageRes = errorMessageRes) }
}