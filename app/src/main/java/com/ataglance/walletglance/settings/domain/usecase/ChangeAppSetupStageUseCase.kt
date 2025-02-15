package com.ataglance.walletglance.settings.domain.usecase

interface ChangeAppSetupStageUseCase {

    suspend fun preFinishSetup()

    suspend fun finishSetup()

    /**
     * Check for right screen setting after the first app setup.
     * If the app has been setup but finish screen was not closed (so 2 is still saved as a start
     * destination in the datastore preferences, which is the finish screen), reassign this
     * preference to 1 (home screen).
     */
    suspend fun updateSetupStageInNeeded()

}