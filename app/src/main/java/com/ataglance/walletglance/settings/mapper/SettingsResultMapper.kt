package com.ataglance.walletglance.settings.mapper

import androidx.annotation.StringRes
import com.ataglance.walletglance.R
import com.ataglance.walletglance.request.domain.model.result.ResultData
import com.ataglance.walletglance.request.presentation.model.ResultState
import com.ataglance.walletglance.settings.errorHandling.SettingsError


fun ResultData<Unit, SettingsError>.toResultWithMessageState(): ResultState.MessageState? {
    return when (this) {
        is ResultData.Success -> null
        is ResultData.Error -> error.toResultWithMessageState()
    }
}


fun SettingsError.toResultWithMessageState(): ResultState.MessageState {
    return ResultState.MessageState(
        isSuccessful = false,
        messageRes = this.asMessageResOrNull()
    )
}


@StringRes private fun SettingsError.asMessageResOrNull(): Int {
    return when (this) {
        SettingsError.NotSaved -> R.string.not_saved
    }
}
