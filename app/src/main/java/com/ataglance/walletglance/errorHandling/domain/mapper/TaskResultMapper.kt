package com.ataglance.walletglance.errorHandling.domain.mapper

import com.ataglance.walletglance.errorHandling.domain.model.TaskResult
import com.ataglance.walletglance.errorHandling.domain.model.TaskResultData

fun <T> TaskResultData<T>.toTaskResult(): TaskResult {
    return TaskResult(isSuccessful = isSuccessful, messageRes = messageRes)
}