package com.ataglance.walletglance.settings.domain.usecase.language

import com.ataglance.walletglance.errorHandling.domain.model.result.ResultData
import com.ataglance.walletglance.settings.errorHandling.SettingsError

interface SaveLanguageRemotelyUseCase {
    suspend fun execute(langCode: String, timestamp: Long): ResultData<Unit, SettingsError>
}