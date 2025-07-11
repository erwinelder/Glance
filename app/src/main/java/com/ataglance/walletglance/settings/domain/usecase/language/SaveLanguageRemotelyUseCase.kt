package com.ataglance.walletglance.settings.domain.usecase.language

import com.ataglance.walletglance.request.domain.model.result.ResultData
import com.ataglance.walletglance.settings.error.SettingsError

interface SaveLanguageRemotelyUseCase {
    suspend fun execute(langCode: String, timestamp: Long): ResultData<Unit, SettingsError>
}