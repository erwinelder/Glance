package com.ataglance.walletglance.settings.domain.usecase.theme

import com.ataglance.walletglance.settings.domain.model.AppThemeConfiguration
import kotlinx.coroutines.flow.Flow

interface GetAppThemeConfigurationUseCase {

    fun getFlow(): Flow<AppThemeConfiguration>

}