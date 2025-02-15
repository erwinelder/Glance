package com.ataglance.walletglance.settings.domain.usecase

interface ChangeAppSetupStatusUseCase {

    suspend fun preFinishSetup()

    suspend fun finishSetup()

    suspend fun updateSetupStageInNeeded()

}