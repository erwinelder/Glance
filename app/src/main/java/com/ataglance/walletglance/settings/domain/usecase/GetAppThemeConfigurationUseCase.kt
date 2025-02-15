package com.ataglance.walletglance.settings.domain.usecase

import com.ataglance.walletglance.settings.domain.model.AppThemeConfiguration
import kotlinx.coroutines.flow.Flow

interface GetAppThemeConfigurationUseCase {

    fun getAsFlow(): Flow<AppThemeConfiguration>

}