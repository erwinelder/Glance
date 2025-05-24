package com.ataglance.walletglance.personalization.domain.usecase.theme

import com.ataglance.walletglance.settings.domain.model.AppThemeConfiguration
import kotlinx.coroutines.flow.Flow

interface GetAppThemeConfigurationUseCase {

    fun getFlow(): Flow<AppThemeConfiguration>

}