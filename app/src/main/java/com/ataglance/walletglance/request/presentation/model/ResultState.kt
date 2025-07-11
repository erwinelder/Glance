package com.ataglance.walletglance.request.presentation.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Stable

sealed class ResultState {

    @Stable
    data class MessageState(
        val isSuccessful: Boolean,
        @StringRes val messageRes: Int
    ) : ResultState()

}